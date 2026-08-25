package com.ruoyi.web.controller.ops;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OpsInspectionRecord;
import com.ruoyi.system.service.IOpsInspectionRecordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡检记录 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ops/record")
public class OpsInspectionRecordController extends BaseController
{
    @Autowired
    private IOpsInspectionRecordService inspectionRecordService;

    /**
     * 获取巡检记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsInspectionRecord inspectionRecord)
    {
        startPage();
        List<OpsInspectionRecord> list = inspectionRecordService.selectInspectionRecordList(inspectionRecord);
        return getDataTable(list);
    }

    @Log(title = "巡检记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ops:record:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsInspectionRecord inspectionRecord)
    {
        List<OpsInspectionRecord> list = inspectionRecordService.selectInspectionRecordList(inspectionRecord);
        ExcelUtil<OpsInspectionRecord> util = new ExcelUtil<OpsInspectionRecord>(OpsInspectionRecord.class);
        util.exportExcel(response, list, "巡检记录数据");
    }

    /**
     * 根据巡检记录编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId)
    {
        return success(inspectionRecordService.selectInspectionRecordById(recordId));
    }

    /**
     * 新增巡检记录
     */
    @PreAuthorize("@ss.hasPermi('ops:record:add')")
    @Log(title = "巡检记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OpsInspectionRecord inspectionRecord)
    {
        inspectionRecord.setCreateBy(getUsername());
        return toAjax(inspectionRecordService.insertInspectionRecord(inspectionRecord));
    }

    /**
     * 修改巡检记录
     */
    @PreAuthorize("@ss.hasPermi('ops:record:edit')")
    @Log(title = "巡检记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OpsInspectionRecord inspectionRecord)
    {
        inspectionRecord.setUpdateBy(getUsername());
        return toAjax(inspectionRecordService.updateInspectionRecord(inspectionRecord));
    }

    /**
     * 删除巡检记录
     */
    @PreAuthorize("@ss.hasPermi('ops:record:remove')")
    @Log(title = "巡检记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(inspectionRecordService.deleteInspectionRecordByIds(recordIds));
    }
}