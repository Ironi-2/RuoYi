<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="78px">
      <el-form-item label="服务器名" prop="serverName">
        <el-input
          v-model="queryParams.serverName"
          placeholder="请输入服务器名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务器IP" prop="serverIp">
        <el-input
          v-model="queryParams.serverIp"
          placeholder="请输入服务器IP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系统类型" prop="osType">
        <el-input
          v-model="queryParams.osType"
          placeholder="请输入操作系统"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="serverStatus">
        <el-select v-model="queryParams.serverStatus" placeholder="服务器状态" clearable>
          <el-option
            v-for="item in serverStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['ops:server:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ops:server:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ops:server:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ops:server:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serverList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务器名称" align="center" prop="serverName" min-width="140" />
      <el-table-column label="服务器IP" align="center" prop="serverIp" min-width="120" />
      <el-table-column label="操作系统" align="center" prop="osType" />
      <el-table-column label="CPU核数" align="center" prop="cpuCore" width="90" />
      <el-table-column label="内存GB" align="center" prop="memorySize" width="90" />
      <el-table-column label="磁盘GB" align="center" prop="diskSize" width="90" />
      <el-table-column label="状态" align="center" prop="serverStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.serverStatus)">{{ statusLabel(scope.row.serverStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="owner" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ops:server:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ops:server:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务器名称" prop="serverName">
              <el-input v-model="form.serverName" placeholder="请输入服务器名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务器IP" prop="serverIp">
              <el-input v-model="form.serverIp" placeholder="请输入服务器IP" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="操作系统" prop="osType">
              <el-input v-model="form.osType" placeholder="请输入操作系统" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="owner">
              <el-input v-model="form.owner" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="CPU核数" prop="cpuCore">
              <el-input-number v-model="form.cpuCore" class="form-number" controls-position="right" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内存GB" prop="memorySize">
              <el-input-number v-model="form.memorySize" class="form-number" controls-position="right" :min="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="磁盘GB" prop="diskSize">
              <el-input-number v-model="form.diskSize" class="form-number" controls-position="right" :min="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="serverStatus">
          <el-radio-group v-model="form.serverStatus">
            <el-radio
              v-for="item in serverStatusOptions"
              :key="item.value"
              :label="item.value"
            >{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listServer, getServer, delServer, addServer, updateServer } from "@/api/ops/server"

export default {
  name: "OpsServer",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      serverList: [],
      title: "",
      open: false,
      serverStatusOptions: [
        { label: "正常", value: "0", type: "success" },
        { label: "停用", value: "1", type: "info" },
        { label: "故障", value: "2", type: "danger" }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serverName: undefined,
        serverIp: undefined,
        osType: undefined,
        serverStatus: undefined
      },
      form: {},
      rules: {
        serverName: [
          { required: true, message: "服务器名称不能为空", trigger: "blur" }
        ],
        serverIp: [
          { required: true, message: "服务器IP不能为空", trigger: "blur" }
        ],
        serverStatus: [
          { required: true, message: "服务器状态不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listServer(this.queryParams).then(response => {
        this.serverList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    statusLabel(value) {
      const item = this.serverStatusOptions.find(item => item.value === value)
      return item ? item.label : value
    },
    statusType(value) {
      const item = this.serverStatusOptions.find(item => item.value === value)
      return item ? item.type : ""
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        serverId: undefined,
        serverName: undefined,
        serverIp: undefined,
        osType: undefined,
        cpuCore: 2,
        memorySize: 4,
        diskSize: 100,
        serverStatus: "0",
        owner: undefined,
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.serverId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加服务器资产"
    },
    handleUpdate(row) {
      this.reset()
      const serverId = row.serverId || this.ids
      getServer(serverId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改服务器资产"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.serverId != undefined) {
            updateServer(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addServer(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const serverIds = row.serverId || this.ids
      this.$modal.confirm('是否确认删除服务器资产编号为"' + serverIds + '"的数据项？').then(function() {
        return delServer(serverIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('ops/server/export', {
        ...this.queryParams
      }, `server_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.form-number {
  width: 100%;
}
</style>
