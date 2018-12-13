package com.stu.system.bean;

import java.util.List;

/**
 * http://fuck.biegral.com/Api/GetStuHisList?sid=b1aac8bc-300e-47a5-baad-c8d13776dd3f&pageindex=1
 */
public class GetStuHisListBean extends BaseBean {

    private List<List<ValueBean>> value;

    public List<List<ValueBean>> getValue() {
        return value;
    }

    public void setValue(List<List<ValueBean>> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * title : 上课
         * intro : 睡觉
         * imgcount : 2
         * path : Uploads/20181207
         * vedio : 1
         * id : 05edf4b4-e9d8-474b-8cb4-d0ce00480dcc
         */

        private String title;
        private String intro;
        private int imgcount;
        private String path;
        private String vedio;
        private String id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getImgcount() {
            return imgcount;
        }

        public void setImgcount(int imgcount) {
            this.imgcount = imgcount;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getVedio() {
            return vedio;
        }

        public void setVedio(String vedio) {
            this.vedio = vedio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
