package com.ruoyi.web.controller.ops;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OpsServerAsset;
import com.ruoyi.system.service.IOpsServerAssetService;

/**
 * 服务器资产 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ops/server")
public class OpsServerAssetController extends BaseController
{
    @Autowired
    private IOpsServerAssetService serverAssetService;

    /**
     * 获取服务器资产列表
     */
    @PreAuthorize("@ss.hasPermi('ops:server:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsServerAsset serverAsset)
    {
        startPage();
        List<OpsServerAsset> list = serverAssetService.selectServerAssetList(serverAsset);
        return getDataTable(list);
    }

    @Log(title = "服务器资产", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('ops:server:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsServerAsset serverAsset)
    {
        List<OpsServerAsset> list = serverAssetService.selectServerAssetList(serverAsset);
        ExcelUtil<OpsServerAsset> util = new ExcelUtil<OpsServerAsset>(OpsServerAsset.class);
        util.exportExcel(response, list, "服务器资产数据");
    }

    /**
     * 根据服务器资产编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:server:query')")
    @GetMapping(value = "/{serverId}")
    public AjaxResult getInfo(@PathVariable Long serverId)
    {
        return success(serverAssetService.selectServerAssetById(serverId));
    }

    /**
     * 新增服务器资产
     */
    @PreAuthorize("@ss.hasPermi('ops:server:add')")
    @Log(title = "服务器资产", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OpsServerAsset serverAsset)
    {
        if (!serverAssetService.checkServerIpUnique(serverAsset))
        {
            return error("新增服务器'" + serverAsset.getServerName() + "'失败，服务器IP已存在");
        }
        serverAsset.setCreateBy(getUsername());
        return toAjax(serverAssetService.insertServerAsset(serverAsset));
    }

    /**
     * 修改服务器资产
     */
    @PreAuthorize("@ss.hasPermi('ops:server:edit')")
    @Log(title = "服务器资产", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OpsServerAsset serverAsset)
    {
        if (!serverAssetService.checkServerIpUnique(serverAsset))
        {
            return error("修改服务器'" + serverAsset.getServerName() + "'失败，服务器IP已存在");
        }
        serverAsset.setUpdateBy(getUsername());
        return toAjax(serverAssetService.updateServerAsset(serverAsset));
    }

    /**
     * 删除服务器资产
     */
    @PreAuthorize("@ss.hasPermi('ops:server:remove')")
    @Log(title = "服务器资产", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serverIds}")
    public AjaxResult remove(@PathVariable Long[] serverIds)
    {
        return toAjax(serverAssetService.deleteServerAssetByIds(serverIds));
    }

    /**
     * 获取服务器选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<OpsServerAsset> servers = serverAssetService.selectServerAssetAll();
        return success(servers);
    }
}