package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OpsServerAsset;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务器资产 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface OpsServerAssetMapper
{
    public List<OpsServerAsset> selectServerAssetList(OpsServerAsset serverAsset);

    public List<OpsServerAsset> selectServerAssetAll();

    public OpsServerAsset selectServerAssetById(Long serverId);

    public OpsServerAsset checkServerIpUnique(String serverIp);

    public int insertServerAsset(OpsServerAsset serverAsset);

    public int updateServerAsset(OpsServerAsset serverAsset);

    public int deleteServerAssetById(Long serverId);

    public int deleteServerAssetByIds(Long[] serverIds);
}