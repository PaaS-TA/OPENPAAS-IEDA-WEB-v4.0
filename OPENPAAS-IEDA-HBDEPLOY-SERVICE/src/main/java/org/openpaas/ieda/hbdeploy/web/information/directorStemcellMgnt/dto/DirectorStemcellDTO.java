package org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.dto;

import org.hibernate.validator.constraints.NotBlank;

public class DirectorStemcellDTO {

    public static class Upload {
        @NotBlank
        private String fileName; //파일명

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
    }
    
    public static class Delete {
        @NotBlank
        private String stemcellName; //스템셀 명
        
        private String version; // 스템셀 버전

        public String getStemcellName() {
            return stemcellName;
        }

        public void setStemcellName(String stemcellName) {
            this.stemcellName = stemcellName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
        
    }
}
