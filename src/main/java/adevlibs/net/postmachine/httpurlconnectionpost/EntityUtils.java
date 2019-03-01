package adevlibs.net.postmachine.httpurlconnectionpost;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public final class EntityUtils {

    /** Disabled default constructor. */
    private EntityUtils() {
    }

    public static byte[] toByteArray(final HttpEntity entity) throws IOException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return new byte[] {};
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        ByteArrayBuffer buffer = new ByteArrayBuffer(i);
        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            instream.close();
        }
        return buffer.toByteArray();
    }

    public static String getContentCharSet(final HttpEntity entity) throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;
    }
    
    public static String getContentCharSet(final URLConnection entity) throws ParseException {
        
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        String contentType = entity.getHeaderField("Content-Type");
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }
        return charset;
    }

    public static String toString(final HttpEntity entity, final String defaultCharset) throws IOException,
            ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }

        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            if (contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            } 
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                int start =0;
                if (buffer.length() == 0) {
                    for (int j = 0; j < tmp.length; j++) {
                        if (tmp[j]=='\r' || tmp[j] == '\n') {
                            start ++;
                            l--;
                        }else {
                            break;
                        }
                    }
                }
                buffer.append(tmp, start, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static String toString(final HttpEntity entity) throws IOException, ParseException {
        return toString(entity, null);
    }
    public static String toString(final URLConnection entity, final String defaultCharset) throws IOException,
    ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getInputStream();
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        
        String contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            if (contentEncoding.equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            }
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                int start =0;
                if (buffer.length() == 0) {
                    for (int j = 0; j < tmp.length; j++) {
                        if (tmp[j]=='\r' || tmp[j] == '\n') {
                            start ++;
                            l--;
                        }else {
                            break;
                        }
                    }
                }
                buffer.append(tmp, start, l);
            }
        } finally {
            reader.close();
            reader = null;
            
            instream.close();
            instream = null;
        }
        return buffer.toString();
    }
    
    public static String toString(final URLConnection entity) throws IOException, ParseException {
        return toString(entity, null);
    }
    
}
