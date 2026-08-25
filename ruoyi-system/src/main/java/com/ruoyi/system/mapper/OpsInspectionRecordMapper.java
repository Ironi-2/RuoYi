package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OpsInspectionRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检记录 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface OpsInspectionRecordMapper
{
    public List<OpsInspectionRecord> selectInspectionRecordList(OpsInspectionRecord inspectionRecord);

    public OpsInspectionRecord selectInspectionRecordById(Long recordId);

    public int countInspectionRecordByServerId(Long serverId);

    public int insertInspectionRecord(OpsInspectionRecord inspectionRecord);

    public int updateInspectionRecord(OpsInspectionRecord inspectionRecord);

    public int deleteInspectionRecordById(Long recordId);

    public int deleteInspectionRecordByIds(Long[] recordIds);
}