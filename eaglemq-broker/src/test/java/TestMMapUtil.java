import org.idea.eaglemq.broker.utils.MMapUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestMMapUtil {

    private MMapUtil mmapUtil;
    private static final String filePath = "F:\\Java\\eaglemq\\broker\\store\\order_cancel_topic\\00000000";

    @Before
    public void setUp() throws IOException{
        mmapUtil = new MMapUtil();
        mmapUtil.loadFileInMMap(filePath,0, 0.1*1024*100);
        System.out.println("File map cache succeed: 0.1m");
    }

    @Test
    public void trestLoadFile() throws IOException{
        //mmapUtil.loadFileInMMap(filePath,0, 100*1024*100);
    }

    @Test
    public void testWriteAndReadFile(){
        String str = "This is a test content";
        byte[] content = str.getBytes();
        mmapUtil.writeContent(str.getBytes());
        byte[] readContent = mmapUtil.readContent(0, content.length+1);
        System.out.println(new String(readContent));
    }

}
