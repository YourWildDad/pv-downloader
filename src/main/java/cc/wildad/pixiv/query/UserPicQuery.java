package cc.wildad.pixiv.query;

import cc.wildad.pixiv.exception.PV_Exception;

import java.util.List;

public interface UserPicQuery extends PicQuery {

    public List<String> query(String id) throws PV_Exception;
}
