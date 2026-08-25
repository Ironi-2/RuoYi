package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OpsServerAsset;

/**
 * 服务器资产 服务层
 *
 * @author ruoyi
 */
public interface IOpsServerAssetService
{
    public List<OpsServerAsset> selectServerAssetList(OpsServerAsset serverAsset);

    public List<OpsServerAsset> selectServerAssetAll();

    public OpsServerAsset selectServerAssetById(Long serverId);

    public boolean checkServerIpUnique(OpsServerAsset serverAsset);

    public int insertServerAsset(OpsServerAsset serverAsset);

    public int updateServerAsset(OpsServerAsset serverAsset);

    public int deleteServerAssetByIds(Long[] serverIds);

    public int deleteServerAssetById(Long serverId);
}