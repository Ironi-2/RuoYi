package com.ruoyi.system.service;



import java.util.List;
import com.ruoyi.system.domain.OpsInspectionRecord;

/**
 * 巡检记录 服务层
 *
 * @author ruoyi
 */
public interface IOpsInspectionRecordService
{
    public List<OpsInspectionRecord> selectInspectionRecordList(OpsInspectionRecord inspectionRecord);

    public OpsInspectionRecord selectInspectionRecordById(Long recordId);

    public int countInspectionRecordByServerId(Long serverId);

    public int insertInspectionRecord(OpsInspectionRecord inspectionRecord);

    public int updateInspectionRecord(OpsInspectionRecord inspectionRecord);

    public int deleteInspectionRecordByIds(Long[] recordIds);

    public int deleteInspectionRecordById(Long recordId);
}