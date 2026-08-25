package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.OpsInspectionRecord;
import com.ruoyi.system.mapper.OpsInspectionRecordMapper;
import com.ruoyi.system.service.IOpsInspectionRecordService;

/**
 * 巡检记录 服务层处理
 *
 * @author ruoyi
 */
@Service
public class OpsInspectionRecordServiceImpl implements IOpsInspectionRecordService
{
    @Autowired
    private OpsInspectionRecordMapper inspectionRecordMapper;

    @Override
    public List<OpsInspectionRecord> selectInspectionRecordList(OpsInspectionRecord inspectionRecord)
    {
        return inspectionRecordMapper.selectInspectionRecordList(inspectionRecord);
    }

    @Override
    public OpsInspectionRecord selectInspectionRecordById(Long recordId)
    {
        return inspectionRecordMapper.selectInspectionRecordById(recordId);
    }

    @Override
    public int countInspectionRecordByServerId(Long serverId)
    {
        return inspectionRecordMapper.countInspectionRecordByServerId(serverId);
    }

    @Override
    public int insertInspectionRecord(OpsInspectionRecord inspectionRecord)
    {
        return inspectionRecordMapper.insertInspectionRecord(inspectionRecord);
    }

    @Override
    public int updateInspectionRecord(OpsInspectionRecord inspectionRecord)
    {
        return inspectionRecordMapper.updateInspectionRecord(inspectionRecord);
    }

    @Override
    public int deleteInspectionRecordByIds(Long[] recordIds)
    {
        return inspectionRecordMapper.deleteInspectionRecordByIds(recordIds);
    }

    @Override
    public int deleteInspectionRecordById(Long recordId)
    {
        return inspectionRecordMapper.deleteInspectionRecordById(recordId);
    }
}