package info.blockchain.wallet.ethereum;

import info.blockchain.wallet.MockedResponseTest;
import info.blockchain.wallet.ethereum.data.EthAddressResponse;
import info.blockchain.wallet.ethereum.data.EthAddressResponseMap;
import info.blockchain.wallet.ethereum.data.EthLatestBlock;
import info.blockchain.wallet.ethereum.data.EthTransaction;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EthAccountApiTest extends MockedResponseTest {

    private EthAccountApi subject = new EthAccountApi();

    @Test
    public void getEthAccount() throws Exception {
        // Arrange
        LinkedList<Pair> responses = new LinkedList<>();
        responses.add(Pair.of(200, ACCOUNT_RESPONSE));
        mockInterceptor.setResponseList(responses);
        // Act
        final TestObserver<EthAddressResponseMap> response =
                subject.getEthAddress(Arrays.asList("address", "address")).test();
        // Assert
        response.assertComplete();
        response.assertNoErrors();
        final Map<String, EthAddressResponse> ethAccountMap = response.values().get(0).getEthAddressResponseMap();

        EthAddressResponse ethAccount = ethAccountMap
            .get("0x879dBFdE84B0239feB355f55F81fb29f898C778C");

        assertEquals(8878260, (int) ethAccount.getId());
        assertEquals("0x879dbfde84b0239feb355f55f81fb29f898c778c", ethAccount.getAccount());
        assertEquals(3, ethAccount.getTransactions().size());
        final EthTransaction transaction = ethAccount.getTransactions().get(1);
        assertEquals(1503130094L, ((long) transaction.getTimeStamp()));
        assertEquals("0x879dbfde84b0239feb355f55f81fb29f898c778c", transaction.getFrom());
        assertEquals("0x0297a2a4cf8117a27b4ad684e43c34e21e600753", transaction.getTo());
    }

    @Test
    public void getIfContract_returns_false() throws Exception {
        // Arrange
        LinkedList<Pair> responses = new LinkedList<>();
        responses.add(Pair.of(200, NO_CONTRACT_RESPONSE));
        mockInterceptor.setResponseList(responses);
        // Act
        final TestObserver<Boolean> response = subject.getIfContract("address").test();
        // Assert
        response.assertComplete();
        response.assertNoErrors();
        final Boolean contract = response.values().get(0);
        assertFalse(contract);
    }

    @Test
    public void getIfContract_returns_true() throws Exception {
        // Arrange
        LinkedList<Pair> responses = new LinkedList<>();
        responses.add(Pair.of(200, CONTRACT_RESPONSE));
        mockInterceptor.setResponseList(responses);
        // Act
        final TestObserver<Boolean> response = subject.getIfContract("address").test();
        // Assert
        response.assertComplete();
        response.assertNoErrors();
        final Boolean contract = response.values().get(0);
        assertTrue(contract);
    }

    @Test
    public void getLatestBlock() throws Exception {
        // Arrange
        LinkedList<Pair> responses = new LinkedList<>();
        responses.add(Pair.of(200, LATEST_BLOCK_RESPONSE));
        mockInterceptor.setResponseList(responses);
        // Act
        final TestObserver<EthLatestBlock> response = subject.getLatestBlock().test();
        // Assert
        response.assertComplete();
        response.assertNoErrors();
        final EthLatestBlock latestBlock = response.values().get(0);
        assertEquals(4272693L, ((long) latestBlock.getBlockHeight()));
    }

    private static final String ACCOUNT_RESPONSE = "{\n" +
            "\t\"0x879dBFdE84B0239feB355f55F81fb29f898C778C\": {\n" +
            "\t\t\"id\": 8878260,\n" +
            "\t\t\"txn_count\": 3,\n" +
            "\t\t\"account\": \"0x879dbfde84b0239feb355f55f81fb29f898c778c\",\n" +
            "\t\t\"accountType\": 0,\n" +
            "\t\t\"balance\": \"187701779412868832\",\n" +
            "\t\t\"nonce\": 1,\n" +
            "\t\t\"firstTime\": 1503064376,\n" +
            "\t\t\"numNormalTxns\": 3,\n" +
            "\t\t\"numInternalTxns\": 0,\n" +
            "\t\t\"totalReceived\": \"851545761963548896\",\n" +
            "\t\t\"totalSent\": \"663402982550680064\",\n" +
            "\t\t\"totalFee\": \"441000000000000\",\n" +
            "\t\t\"createdBy\": null,\n" +
            "\t\t\"createdIn\": null,\n" +
            "\t\t\"txns\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"blockNumber\": 4213188,\n" +
            "\t\t\t\t\"timeStamp\": 1503932683,\n" +
            "\t\t\t\t\"hash\": \"0x99c14ac55e4d8066c2ca743eee586bf7e2c9a70cc0faa63c62f0ec20205f956d\",\n" +
            "\t\t\t\t\"failFlag\": false,\n" +
            "\t\t\t\t\"errorDescription\": null,\n" +
            "\t\t\t\t\"nonce\": \"0x11\",\n" +
            "\t\t\t\t\"blockHash\": \"0x96000614cedddc1c30d5ea8857ce76fd29e3aa1139051bad6b6a4a178c493ce2\",\n" +
            "\t\t\t\t\"transactionIndex\": 24,\n" +
            "\t\t\t\t\"from\": \"0x74d62f5ac3423c6a28aaff458be3ef807137696b\",\n" +
            "\t\t\t\t\"to\": \"0x879dbfde84b0239feb355f55f81fb29f898c778c\",\n" +
            "\t\t\t\t\"value\": \"187701779412868832\",\n" +
            "\t\t\t\t\"gas\": 21000,\n" +
            "\t\t\t\t\"gasPrice\": 21000000000,\n" +
            "\t\t\t\t\"gasUsed\": 21000,\n" +
            "\t\t\t\t\"input\": \"0x\",\n" +
            "\t\t\t\t\"internalFlag\": false,\n" +
            "\t\t\t\t\"contractAddress\": null\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"blockNumber\": 4176736,\n" +
            "\t\t\t\t\"timeStamp\": 1503130094,\n" +
            "\t\t\t\t\"hash\": \"0xcc6952c8f5c6e90d1addcaf3717b6df251982637f0cafc32c7f6348018dd2a7b\",\n" +
            "\t\t\t\t\"failFlag\": false,\n" +
            "\t\t\t\t\"errorDescription\": null,\n" +
            "\t\t\t\t\"nonce\": \"0x0\",\n" +
            "\t\t\t\t\"blockHash\": \"0x33a980e70dd3951f85db42cfdddac4d320284d259c6e684ffb179d21586b666f\",\n" +
            "\t\t\t\t\"transactionIndex\": 66,\n" +
            "\t\t\t\t\"from\": \"0x879dbfde84b0239feb355f55f81fb29f898c778c\",\n" +
            "\t\t\t\t\"to\": \"0x0297a2a4cf8117a27b4ad684e43c34e21e600753\",\n" +
            "\t\t\t\t\"value\": \"663402982550680064\",\n" +
            "\t\t\t\t\"gas\": 21000,\n" +
            "\t\t\t\t\"gasPrice\": 21000000000,\n" +
            "\t\t\t\t\"gasUsed\": 21000,\n" +
            "\t\t\t\t\"input\": \"0x\",\n" +
            "\t\t\t\t\"internalFlag\": false,\n" +
            "\t\t\t\t\"contractAddress\": null\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"blockNumber\": 4173545,\n" +
            "\t\t\t\t\"timeStamp\": 1503064376,\n" +
            "\t\t\t\t\"hash\": \"0x982a1349b446b2613b911e8c0247cb209bfa6b5746581dc9025efbedc7b67996\",\n" +
            "\t\t\t\t\"failFlag\": false,\n" +
            "\t\t\t\t\"errorDescription\": null,\n" +
            "\t\t\t\t\"nonce\": \"0x0\",\n" +
            "\t\t\t\t\"blockHash\": \"0x4f15dd1e327130121b9900d6ec99f28623a459478abe28161dafb61eb314dbf7\",\n" +
            "\t\t\t\t\"transactionIndex\": 58,\n" +
            "\t\t\t\t\"from\": \"0x74d62f5ac3423c6a28aaff458be3ef807137696b\",\n" +
            "\t\t\t\t\"to\": \"0x879dbfde84b0239feb355f55f81fb29f898c778c\",\n" +
            "\t\t\t\t\"value\": \"663843982550680064\",\n" +
            "\t\t\t\t\"gas\": 21000,\n" +
            "\t\t\t\t\"gasPrice\": 21000000000,\n" +
            "\t\t\t\t\"gasUsed\": 21000,\n" +
            "\t\t\t\t\"input\": \"0x\",\n" +
            "\t\t\t\t\"internalFlag\": false,\n" +
            "\t\t\t\t\"contractAddress\": null\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"txnOffset\": 0\n" +
            "\t},\n" +
            "\t\"0xF85608F8fe3887Dab333Ec250A972C1DC19C52B3\": {\n" +
            "\t\t\"accountType\": -1,\n" +
            "\t\t\"balance\": \"0\",\n" +
            "\t\t\"nonce\": 0,\n" +
            "\t\t\"firstTime\": -1,\n" +
            "\t\t\"numNormalTxns\": \"0\",\n" +
            "\t\t\"totalReceived\": \"0\",\n" +
            "\t\t\"totalSent\": \"0\",\n" +
            "\t\t\"totalFee\": \"0\",\n" +
            "\t\t\"createdBy\": \"-1\",\n" +
            "\t\t\"createdIn\": \"-1\",\n" +
            "\t\t\"txns\": [],\n" +
            "\t\t\"txnOffset\": 0,\n" +
            "\t\t\"account\": \"0xF85608F8fe3887Dab333Ec250A972C1DC19C52B3\"\n" +
            "\t}\n" +
            "}";

    private static final String NO_CONTRACT_RESPONSE = "{\n" +
            "\t\"contract\": false\n" +
            "}";

    private static final String CONTRACT_RESPONSE = "{\n" +
            "\t\"contract\": true\n" +
            "}";

    private static final String LATEST_BLOCK_RESPONSE = "{\n" +
            "  \"number\": 4272693,\n" +
            "  \"hash\": \"0x6d77039178b68664d48c3946b5525ea62d1380c9fd988a3999662d431f20b62f\",\n" +
            "  \"parentHash\": \"0x08bbf2f3e03f89b3cdba9720cbc7fcab14d9024d0331789a02b392524c5ced1e\",\n" +
            "  \"nonce\": 17644216103464599520,\n" +
            "  \"sha3Uncles\": \"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347\",\n" +
            "  \"logsBloom\": \"0x5090400024c801b8040708044004c14210005000a802121003b1050041a0018a000229141061030328c0260000c0242b202780100044108904000822aa218206000800402c5184009100300e2048040062040001012c4001084912008266434420024020724461840040a160820808860061002458048840800401102a90200c2020020100000020a00004000500001045240881018000a082120400000423060200020804001000b20060806300802080050581201180210800000002102089c80105c301062040002c44a00240002080121084802c4004a415300424082145049440801011012000080066800002a4401c014008020058010000820820129a\",\n" +
            "  \"transactionsRoot\": \"0xaae344a412c96dd27600e347584b0f1cb5992fad659be2af02d33fff1d1f0796\",\n" +
            "  \"stateRoot\": \"0x666e20c6b20b77e143312537633dc8fc0aef3ed49834f8e1250d9e3a0410c0ab\",\n" +
            "  \"receiptsRoot\": \"0x7d62d603a32ceb113dd012f4c015627001dc15199f41a3d3d0f9be6de8bcb8e0\",\n" +
            "  \"author\": null,\n" +
            "  \"miner\": \"0xea674fdde714fd979de3edf0f56aa9716b898ec8\",\n" +
            "  \"mixHash\": \"0xe990143c0bdf54274c8e2830121dc90e7ef2e11ffb144595601f0f593a9f267c\",\n" +
            "  \"difficulty\": 2264942935176218,\n" +
            "  \"totalDifficulty\": 928317937389649709684,\n" +
            "  \"extraData\": \"0x65746865726d696e652d6173696135\",\n" +
            "  \"size\": 23935,\n" +
            "  \"gasLimit\": 6712392,\n" +
            "  \"gasUsed\": 6695785,\n" +
            "  \"timestamp\": 1505383570,\n" +
            "  \"transactions\": []\n" +
            "}";

}