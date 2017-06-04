package test.lucene.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * �����ļ���
 * @author chuzhen
 *
 * 2017��6��4��
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
