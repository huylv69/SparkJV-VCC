import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

/**
 * @author huylv
 */
public class Service {

    public static void main(String[] args) {
        port(8080);

        LoadingCache<Integer, ArrayList> arrayCache =
                CacheBuilder.newBuilder()
                        .maximumSize(100)                             // maximum 100 records can be cached
                        .expireAfterWrite(20, TimeUnit.SECONDS)      // cache will expire after 20 SECONDS of access
                        .expireAfterAccess(10, TimeUnit.SECONDS)
                        .build(new CacheLoader<Integer, ArrayList>() {
                            @Override
                            public ArrayList load(Integer n) throws Exception {
                                System.out.println("Init "+n);
                                ArrayList result = new ArrayList();
                                int low = 2, high = n;
                                while (low < high+1) {
                                    boolean flag = false;
                                    for(int i = 2; i <= low/2; ++i) {
                                        if(low % i == 0) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag)
                                        result.add(low);
                                    ++low;
                                }
                                return result;
                            }  // build the cacheloader

                        });
        get("/prime", (req, res) -> {
            Integer n  = Integer.valueOf(req.queryParams("n"));
            return arrayCache.get(n);
        });
        * Chinh sua tren server github */
    }
}
