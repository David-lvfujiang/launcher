package com.fenda.common.router;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/27 16:01
 * @Description Arouter框架的注解的path 路径 统一在这个类里管理
 */
public interface RouterPath {
    /**
     * 都用这种方式 Voice表示自己的组件名称
     */
    interface Voice{

    }


    public interface Gallery {
        String GALLERY_CATOGORY = "/gallery/GalleryCategoryActivity";
        String GALLERY_LIST_LOCAL = "/gallery/PhotosActivity";
        String GALLERY_LIST_SERVER = "/gallery/FamilyGalleryActivity";
        String GALLERY_DETAIL = "/gallery/PhotoDetailActivity";
    }

}
