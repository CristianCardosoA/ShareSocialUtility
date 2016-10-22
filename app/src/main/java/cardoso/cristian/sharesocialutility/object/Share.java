package cardoso.cristian.sharesocialutility.object;

/**
 * Created by macbook on 22/10/16.
 */

public class Share {
    int image;
    String appComercialName;
    String aplicacionName;

    public Share(int image, String appComercialName, String aplicacionName) {
        this.image = image;
        this.appComercialName = appComercialName;
        this.aplicacionName = aplicacionName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAppComercialName() {
        return appComercialName;
    }

    public void setAppComercialName(String appComercialName) {
        this.appComercialName = appComercialName;
    }

    public String getAplicacionName() {
        return aplicacionName;
    }

    public void setAplicacionName(String aplicacionName) {
        this.aplicacionName = aplicacionName;
    }
}