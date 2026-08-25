package com.ruoyi.system.domain;

import jakarta.validation.constraints.NotBlank;    // 非空校验注解（用于String）
import jakarta.validation.constraints.Size;         // 长度校验注解
import org.apache.commons.lang3.builder.ToStringBuilder;  // toString工具
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;           // 若依Excel导出注解
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;     // 若依实体基类

/**
 * 服务器资产对象 ops_server_asset
 * 对应数据库表 ops_server_asset
 */
public class OpsServerAsset extends BaseEntity {     // 继承BaseEntity获得公共字段

    private static final long serialVersionUID = 1L; // 序列化版本号

    /** 服务器ID — 主键 */
    @Excel(name = "服务器ID", cellType = ColumnType.NUMERIC) // 导出时列名为"服务器ID"，数字类型
    private Long serverId;

    /** 服务器名称 */
    @Excel(name = "服务器名称")                       // Excel导出列名
    private String serverName;

    /** 服务器IP */
    @Excel(name = "服务器IP")
    private String serverIp;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String osType;

    /** CPU核心数 */
    @Excel(name = "CPU核心数")
    private Integer cpuCore;                          // 用Integer而非int，允许null

    /** 内存大小GB */
    @Excel(name = "内存大小GB")
    private Integer memorySize;

    /** 磁盘大小GB */
    @Excel(name = "磁盘大小GB")
    private Integer diskSize;

    /** 状态（0正常 1停用 2故障） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用,2=故障") // 导出时0显示为"正常"
    private String serverStatus;

    /** 负责人 */
    @Excel(name = "负责人")
    private String owner;

    // ==================== getter/setter ====================

    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }

    // @NotBlank：校验不能为null且trim后长度>0（仅用于String）
    // @Size：校验长度范围
    // 注意：若依框架约定，校验注解放在getter方法上
    @NotBlank(message = "服务器名称不能为空")
    @Size(min = 0, max = 100, message = "服务器名称长度不能超过100个字符")
    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    @NotBlank(message = "服务器IP不能为空")
    @Size(min = 0, max = 50, message = "服务器IP长度不能超过50个字符")
    public String getServerIp() { return serverIp; }
    public void setServerIp(String serverIp) { this.serverIp = serverIp; }

    @Size(min = 0, max = 50, message = "操作系统长度不能超过50个字符")
    public String getOsType() { return osType; }
    public void setOsType(String osType) { this.osType = osType; }

    public Integer getCpuCore() { return cpuCore; }
    public void setCpuCore(Integer cpuCore) { this.cpuCore = cpuCore; }

    public Integer getMemorySize() { return memorySize; }
    public void setMemorySize(Integer memorySize) { this.memorySize = memorySize; }

    public Integer getDiskSize() { return diskSize; }
    public void setDiskSize(Integer diskSize) { this.diskSize = diskSize; }

    @NotBlank(message = "服务器状态不能为空")
    public String getServerStatus() { return serverStatus; }
    public void setServerStatus(String serverStatus) { this.serverStatus = serverStatus; }

    @Size(min = 0, max = 50, message = "负责人长度不能超过50个字符")
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    // toString() — 使用Apache Commons Lang3的ToStringBuilder，输出多行格式
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("serverId", getServerId())
                .append("serverName", getServerName())
                .append("serverIp", getServerIp())
                .append("osType", getOsType())
                .append("cpuCore", getCpuCore())
                .append("memorySize", getMemorySize())
                .append("diskSize", getDiskSize())
                .append("serverStatus", getServerStatus())
                .append("owner", getOwner())
                .append("createBy", getCreateBy())      // 来自BaseEntity
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}