package mt.com.go.deploymentsmanagement.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class OSFile implements Serializable {

    private String fileName;
    private String fileExtension;
    private String fullPath;

    private long createdTime;
    private long lastModifiedTime;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((fullPath == null) ? 0 : fullPath.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OSFile)) {
            return false;
        }

        OSFile other = (OSFile) object;
        if ((!this.fileName.equals(other.fileName))
                || (!this.fileExtension.equals(other.fileExtension))
                || (!this.fullPath.equals(other.fullPath))
                || (createdTime != other.createdTime)
                || (lastModifiedTime != other.lastModifiedTime)
        ) {
            return false;
        }
        return true;
    }

}
