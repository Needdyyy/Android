
package com.needyyy.app.Modules.adsAndPage.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckSumPojo implements Serializable {

    @SerializedName("CHECKSUMHASH")
    @Expose
    private String cHECKSUMHASH;

    public String getCHECKSUMHASH() {
        return cHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String cHECKSUMHASH) {
        this.cHECKSUMHASH = cHECKSUMHASH;
    }

}
