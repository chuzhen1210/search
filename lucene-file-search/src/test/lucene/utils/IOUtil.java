package test.lucene.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 处理文件流
 * @author chuzhen
 *
 * 2017年6月4日
 */
public class IOUtil {
	
	public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
