package mvulpe.scanner;

/**
 * Created by Mihai on 06/01/2017.
 */

public class ScanResult {
    private String timestamp;
    private String content;

    public ScanResult(String timestamp,String content){
        this.content=content;
        this.timestamp=timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public void setContent(String timestamp){
        this.content=content;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getContent(){
        return content;
    }
}
