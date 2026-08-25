package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.service.IOpsServerAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OpsServerAsset;
import com.ruoyi.system.mapper.OpsInspectionRecordMapper;
import com.ruoyi.system.mapper.OpsServerAssetMapper;

/**
 * 服务器资产 服务层处理
 *
 * @author ruoyi
 */
@Service
public class OpsServerAssetServiceImpl implements IOpsServerAssetService
{
    @Autowired
    private OpsServerAssetMapper serverAssetMapper;

    @Autowired
    private OpsInspectionRecordMapper inspectionRecordMapper;

    @Override
    public List<OpsServerAsset> selectServerAssetList(OpsServerAsset serverAsset)
    {
        return serverAssetMapper.selectServerAssetList(serverAsset);
    }

    @Override
    public List<OpsServerAsset> selectServerAssetAll()
    {
        return serverAssetMapper.selectServerAssetAll();
    }

    @Override
    public OpsServerAsset selectServerAssetById(Long serverId)
    {
        return serverAssetMapper.selectServerAssetById(serverId);
    }

    @Override
    public boolean checkServerIpUnique(OpsServerAsset serverAsset)
    {
        Long serverId = StringUtils.isNull(serverAsset.getServerId()) ? -1L : serverAsset.getServerId();
        OpsServerAsset info = serverAssetMapper.checkServerIpUnique(serverAsset.getServerIp());
        if (StringUtils.isNotNull(info) && info.getServerId().longValue() != serverId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertServerAsset(OpsServerAsset serverAsset)
    {
        return serverAssetMapper.insertServerAsset(serverAsset);
    }

    @Override
    public int updateServerAsset(OpsServerAsset serverAsset)
    {
        return serverAssetMapper.updateServerAsset(serverAsset);
    }

    @Override
    public int deleteServerAssetByIds(Long[] serverIds)
    {
        for (Long serverId : serverIds)
        {
            OpsServerAsset serverAsset = selectServerAssetById(serverId);
            if (inspectionRecordMapper.countInspectionRecordByServerId(serverId) > 0)
            {
                throw new ServiceException(String.format("%1$s存在巡检记录,不能删除", serverAsset.getServerName()));
            }
        }
        return serverAssetMapper.deleteServerAssetByIds(serverIds);
    }

    @Override
    public int deleteServerAssetById(Long serverId)
    {
        return serverAssetMapper.deleteServerAssetById(serverId);
    }
}