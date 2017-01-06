package org.base.platform;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.BaseUtils;
import org.base.platform.utils.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testObjectToJson() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.filePaths.add("file://sd/dd");
        httpRequestPackage.filePaths.add("file://sd/dc");
        httpRequestPackage.method = HttpMethod.GET;
        httpRequestPackage.url = "http://www.baidu.com";
        httpRequestPackage.params.put("p1", "hello");
        httpRequestPackage.params.put("p2", "world");
        System.out.println(JsonUtils.objToJson(httpRequestPackage));
    }

    @Test
    public void testJsonToObject() {
        String json = "{\"cacheTime\":3000,\"filePaths\":[\"file://sd/dd\",\"file://sd/dc\"],\"id\":124332,\"isSilentRequest\":true,\"method\":\"GET\",\"params\":{\"p1\":\"hello\",\"p2\":\"world\"},\"uniKey\":12312,\"url\":\"http://www.baidu.com\"}\n";
        HttpRequestPackage httpRequestPackage = JsonUtils.jsonToObj(json, HttpRequestPackage.class);
        System.out.println(httpRequestPackage.method == HttpMethod.GET);
    }

    @Test
    public void testListToJson() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.filePaths.add("file://sd/dd");
        httpRequestPackage.filePaths.add("file://sd/dc");
        httpRequestPackage.method = HttpMethod.GET;
        httpRequestPackage.url = "http://www.baidu.com";
        httpRequestPackage.params.put("p1", "hello");
        httpRequestPackage.params.put("p2", "world");
        ArrayList<HttpRequestPackage> list = new ArrayList<>();
        list.add(httpRequestPackage);
        list.addAll((List<HttpRequestPackage>) BaseUtils.deepCopy(list));
        System.out.println(JsonUtils.listToJson(list));
    }

    @Test
    public void testJsonToList() {
        String json = "[{\"cacheTime\":3000,\"filePaths\":[\"file://sd/dd\",\"file://sd/dc\"],\"id\":124332,\"isSilentRequest\":true,\"method\":\"GET\",\"params\":{\"p1\":\"hello\",\"p2\":\"world\"},\"uniKey\":12312,\"url\":\"http://www.baidu.com\"},{\"cacheTime\":3000,\"filePaths\":[\"file://sd/dd\",\"file://sd/dc\"],\"id\":124332,\"isSilentRequest\":true,\"method\":\"GET\",\"params\":{\"p1\":\"hello\",\"p2\":\"world\"},\"uniKey\":12312,\"url\":\"http://www.baidu.com\"}]\n";
        List<HttpRequestPackage> httpRequestPackages = JsonUtils.jsonToList(json, HttpRequestPackage.class);
        System.out.println(httpRequestPackages.get(0).method == HttpMethod.GET);
        System.out.println(httpRequestPackages.get(1).method == HttpMethod.GET);
    }

    @Test
    public void testDeepCopy() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.filePaths.add("file://sd/dd");
        httpRequestPackage.filePaths.add("file://sd/dc");
        httpRequestPackage.method = HttpMethod.GET;
        httpRequestPackage.url = "http://www.baidu.com";
        httpRequestPackage.params.put("p1", "hello");
        httpRequestPackage.params.put("p2", "world");
        System.out.println(httpRequestPackage);
        System.out.println(BaseUtils.deepCopy(httpRequestPackage));
    }
}