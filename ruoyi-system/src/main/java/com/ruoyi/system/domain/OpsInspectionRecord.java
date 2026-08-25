package com.ruoyi.system.domain;

import java.math.BigDecimal;                          // 精确数值类型，用于百分比
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;        // 非null校验（用于非String类型）
import com.fasterxml.jackson.annotation.JsonFormat;   // JSON日期格式化
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡检记录对象 ops_inspection_record
 */
public class OpsInspectionRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录ID — 主键 */
    @Excel(name = "记录ID", cellType = ColumnType.NUMERIC)
    private Long recordId;

    /** 服务器ID — 外键，关联ops_server_asset */
    @Excel(name = "服务器ID", cellType = ColumnType.NUMERIC)
    private Long serverId;

    /** 服务器名称 — 非本表字段，通过LEFT JOIN从ops_server_asset获取 */
    @Excel(name = "服务器名称")
    private String serverName;                        // 冗余字段，仅用于展示

    /** 服务器IP — 非本表字段，通过LEFT JOIN从ops_server_asset获取 */
    @Excel(name = "服务器IP")
    private String serverIp;                          // 冗余字段，仅用于展示

    /** 巡检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")     // JSON序列化为指定日期格式
    @Excel(name = "巡检时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /** CPU使用率 */
    @Excel(name = "CPU使用率", suffix = "%")         // 导出时自动加%后缀
    private BigDecimal cpuUsage;                      // decimal(5,2) 对应 BigDecimal

    /** 内存使用率 */
    @Excel(name = "内存使用率", suffix = "%")
    private BigDecimal memoryUsage;

    /** 磁盘使用率 */
    @Excel(name = "磁盘使用率", suffix = "%")
    private BigDecimal diskUsage;

    /** 巡检状态（0正常 1异常） */
    @Excel(name = "巡检状态", readConverterExp = "0=正常,1=异常")
    private String checkStatus;

    /** 问题描述 */
    @Excel(name = "问题描述")
    private String problemDesc;

    /** 巡检人 */
    @Excel(name = "巡检人")
    private String checkUser;

    // ==================== getter/setter ====================

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    @NotNull(message = "服务器不能为空")               // Long类型用@NotNull，不能用@NotBlank
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }

    // serverName和serverIp无校验注解，因为它们不由前端传入，由SQL JOIN填充
    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    public String getServerIp() { return serverIp; }
    public void setServerIp(String serverIp) { this.serverIp = serverIp; }

    @NotNull(message = "巡检时间不能为空")             // Date类型用@NotNull
    public Date getCheckTime() { return checkTime; }
    public void setCheckTime(Date checkTime) { this.checkTime = checkTime; }

    public BigDecimal getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(BigDecimal cpuUsage) { this.cpuUsage = cpuUsage; }

    public BigDecimal getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(BigDecimal memoryUsage) { this.memoryUsage = memoryUsage; }

    public BigDecimal getDiskUsage() { return diskUsage; }
    public void setDiskUsage(BigDecimal diskUsage) { this.diskUsage = diskUsage; }

    @NotBlank(message = "巡检状态不能为空")
    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }

    public String getProblemDesc() { return problemDesc; }
    public void setProblemDesc(String problemDesc) { this.problemDesc = problemDesc; }

    public String getCheckUser() { return checkUser; }
    public void setCheckUser(String checkUser) { this.checkUser = checkUser; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("recordId", getRecordId())
                .append("serverId", getServerId())
                .append("serverName", getServerName())
                .append("serverIp", getServerIp())
                .append("checkTime", getCheckTime())
                .append("cpuUsage", getCpuUsage())
                .append("memoryUsage", getMemoryUsage())
                .append("diskUsage", getDiskUsage())
                .append("checkStatus", getCheckStatus())
                .append("problemDesc", getProblemDesc())
                .append("checkUser", getCheckUser())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}