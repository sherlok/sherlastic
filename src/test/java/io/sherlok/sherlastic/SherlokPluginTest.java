package io.sherlok.sherlastic;

import static org.codelibs.elasticsearch.runner.ElasticsearchClusterRunner.newConfigs;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.codelibs.elasticsearch.runner.ElasticsearchClusterRunner;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.index.get.GetField;
import org.junit.Assert;

public class SherlokPluginTest extends TestCase {

    private ElasticsearchClusterRunner runner;

    @Override
    protected void setUp() throws Exception {
        // create runner instance
        runner = new ElasticsearchClusterRunner();
        // create ES nodes
        runner.onBuild(new ElasticsearchClusterRunner.Builder() {
            @Override
            public void build(final int number, final Builder settingsBuilder) {
            }
        }).build(
                newConfigs().ramIndexStore().numOfNode(1)
                        .clusterName(UUID.randomUUID().toString()));

        // wait for yellow status
        runner.ensureYellow();
    }

    @Override
    protected void tearDown() throws Exception {
        // close runner
        runner.close();
        // delete all files
        runner.clean();
    }

    public static String getContent(String name) throws IOException {
        return IOUtils.toString(
                SherlokPluginTest.class.getResourceAsStream(name), "UTF-8");
    }

    public void test_runEs() throws Exception {

        final String index = "test_index";
        final String type = "test_type";

        {
            // create an index
            final String indexSettings = getContent("index_settings.json");
            runner.createIndex(index, ImmutableSettings.builder()
                    .loadFromSource(indexSettings).build());
            runner.ensureYellow(index);
            // mapping
            String mapping = getContent("mapping.json");
            runner.createMapping(index, type, mapping);
        }

        if (!runner.indexExists(index)) {
            fail();
        }

        // create 1000 documents
        for (int i = 1; i <= 1000; i++) {
            final IndexResponse indexResponse1 = runner.insert(index, type,
                    String.valueOf(i), "{\"id\":\"" + i + "\",\"msg\":\"test "
                            + i % 100 + "\"}");
            assertTrue(indexResponse1.isCreated());
        }
        runner.refresh();

        final Client client = runner.client();

        test_get(client, index, type, "1", new byte[] { 82, 56, -67, -10, 55,
                -89, -85, -73, 90, -35, -93, 74, 77, -121, 60, -55 },
                new byte[] { 125, 73, 13, -20, -83, 34, -120, -63, -23, -44,
                        -52, 98, 25, 121, -56, 107 }, new byte[] { 91, -99,
                        105, 16, -5, -118, -14, -36 });

        test_get(client, index, type, "2", new byte[] { 0, 96, 125, -3, -121,
                -89, -5, 39, -1, -108, 27, -55, 42, -45, 29, 64 }, new byte[] {
                -15, 40, 77, 111, -91, 21, 10, 3, -31, -41, -84, -79, 57, -35,
                -117, 123 }, new byte[] { -117, 93, 96, 36, 123, 24, -1, 60 });

        test_get(client, index, type, "101", new byte[] { 82, 56, -67, -10, 55,
                -89, -85, -73, 90, -35, -93, 74, 77, -121, 60, -55 },
                new byte[] { 125, 73, 13, -20, -83, 34, -120, -63, -23, -44,
                        -52, 98, 25, 121, -56, 107 }, new byte[] { 91, -99,
                        105, 16, -5, -118, -14, -36 });

    }

    private void test_get(final Client client, final String index,
            final String type, final String id, final byte[] hash1,
            final byte[] hash2, final byte[] hash3) {
        final GetResponse response = client
                .prepareGet(index, type, id)
                .setFields("_source", "minhash_value1", "minhash_value2",
                        "minhash_value3").execute().actionGet();
        assertTrue(response.isExists());
        final Map<String, Object> source = response.getSourceAsMap();
        assertEquals("test " + Integer.parseInt(id) % 100, source.get("msg"));

        final GetField field1 = response.getField("minhash_value1");
        final BytesArray value1 = (BytesArray) field1.getValue();
        assertEquals(hash1.length, value1.length());
        Assert.assertArrayEquals(hash1, value1.array());

        final GetField field2 = response.getField("minhash_value2");
        final BytesArray value2 = (BytesArray) field2.getValue();
        assertEquals(hash2.length, value2.length());
        Assert.assertArrayEquals(hash2, value2.array());

        final GetField field3 = response.getField("minhash_value3");
        final BytesArray value3 = (BytesArray) field3.getValue();
        assertEquals(hash3.length, value3.length());
        Assert.assertArrayEquals(hash3, value3.array());
    }
}
