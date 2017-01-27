package info.blockchain.wallet.bip44;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WalletFactory.java : Class for creating/restoring/reading BIP44 HD wallet
 *
 * BIP44 extension of Bitcoinj
 */
public class WalletFactory {

    public static final String BIP39_ENGLISH_SHA256 = "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db";

    private final Logger mLogger = LoggerFactory.getLogger(WalletFactory.class);

    private Locale locale = null;
    private AbstractBitcoinNetParams networkParameters;

    public WalletFactory(AbstractBitcoinNetParams networkParameters) {
        this.networkParameters = networkParameters;
        this.locale = new Locale("en", "US");
    }

    /**
     * Create new wallet.
     *
     * @param nbWords number of words in menmonic
     * @param passphrase optional BIP39 passphrase
     * @param nbAccounts create this number of accounts
     * @return Wallet
     */
    public Wallet newWallet(int nbWords, String passphrase,
        int nbAccounts) throws IOException, MnemonicException.MnemonicLengthException {

        Wallet hdw;

        if ((nbWords % 3 != 0) || (nbWords < 12 || nbWords > 24)) {
            nbWords = 12;
        }

        // len == 16 (12 words), len == 24 (18 words), len == 32 (24 words)
        int len = (nbWords / 3) * 4;

        if (passphrase == null) {
            passphrase = "";
        }

        SecureRandom random = new SecureRandom();
        byte seed[] = new byte[len];
        random.nextBytes(seed);

        InputStream wis = this.getClass()
            .getClassLoader()
            .getResourceAsStream("wordlist/" + locale.toString() + ".txt");
        if (wis != null) {
            MnemonicCode mc = new MnemonicCode(wis, null);
            hdw = new Wallet(mc, networkParameters, seed, passphrase, nbAccounts);
            wis.close();
        } else {
            mLogger.info("cannot read BIP39 word list");
            return null;
        }

        return hdw;
    }

    /**
     * Restore wallet.
     *
     * @param data: either BIP39 mnemonic or hex seed
     * @param passphrase optional BIP39 passphrase
     * @param nbAccounts create this number of accounts
     * @return Wallet
     */
    public Wallet restoreWallet(String data, String passphrase,
        int nbAccounts)
        throws AddressFormatException, IOException, DecoderException, MnemonicException.MnemonicLengthException, MnemonicException.MnemonicWordException, MnemonicException.MnemonicChecksumException {

        Wallet hdw;

        if (passphrase == null) {
            passphrase = "";
        }

        InputStream wis = this.getClass().getClassLoader()
            .getResourceAsStream("wordlist/" + locale.toString() + ".txt");

        if(wis == null){
            throw new MnemonicException.MnemonicWordException("cannot read BIP39 word list");
        }

        List<String> words;

        MnemonicCode mc;
        mc = new MnemonicCode(wis, null);

        byte[] seed;
        if (data.length() % 4 == 0 && !data.contains(" ")) {
            //Hex seed
            seed = Hex.decodeHex(data.toCharArray());
            hdw = new Wallet(mc, networkParameters, seed, passphrase, nbAccounts);
        } else {
            data = data.replaceAll("[^a-z]+", " ");             // only use for BIP39 English
            words = Arrays.asList(data.trim().split("\\s+"));
            seed = mc.toEntropy(words);
            hdw = new Wallet(mc, networkParameters, seed, passphrase, nbAccounts);
        }

        wis.close();

        return hdw;
    }
}