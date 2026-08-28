<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="78px">
      <el-form-item label="服务器" prop="serverId">
        <el-select v-model="queryParams.serverId" placeholder="请选择服务器" clearable filterable>
          <el-option
            v-for="server in serverOptions"
            :key="server.serverId"
            :label="server.serverName + '（' + server.serverIp + '）'"
            :value="server.serverId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="服务器名" prop="serverName">
        <el-input
          v-model="queryParams.serverName"
          placeholder="请输入服务器名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="巡检状态" clearable>
          <el-option
            v-for="item in checkStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检人" prop="checkUser">
        <el-input
          v-model="queryParams.checkUser"
          placeholder="请输入巡检人"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['ops:record:add']"
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
          v-hasPermi="['ops:record:edit']"
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
          v-hasPermi="['ops:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ops:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务器名称" align="center" prop="serverName" min-width="140" />
      <el-table-column label="服务器IP" align="center" prop="serverIp" min-width="120" />
      <el-table-column label="巡检时间" align="center" prop="checkTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="CPU%" align="center" prop="cpuUsage" width="90" />
      <el-table-column label="内存%" align="center" prop="memoryUsage" width="90" />
      <el-table-column label="磁盘%" align="center" prop="diskUsage" width="90" />
      <el-table-column label="状态" align="center" prop="checkStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.checkStatus === '0' ? 'success' : 'danger'">
            {{ checkStatusLabel(scope.row.checkStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="问题描述" align="center" prop="problemDesc" min-width="160" show-overflow-tooltip />
      <el-table-column label="巡检人" align="center" prop="checkUser" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ops:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ops:record:remove']"
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
        <el-form-item label="服务器" prop="serverId">
          <el-select v-model="form.serverId" class="form-select" placeholder="请选择服务器" filterable>
            <el-option
              v-for="server in serverOptions"
              :key="server.serverId"
              :label="server.serverName + '（' + server.serverIp + '）'"
              :value="server.serverId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检时间" prop="checkTime">
          <el-date-picker
            v-model="form.checkTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择巡检时间"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="CPU%" prop="cpuUsage">
              <el-input-number v-model="form.cpuUsage" class="form-number" controls-position="right" :min="0" :max="100" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内存%" prop="memoryUsage">
              <el-input-number v-model="form.memoryUsage" class="form-number" controls-position="right" :min="0" :max="100" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="磁盘%" prop="diskUsage">
              <el-input-number v-model="form.diskUsage" class="form-number" controls-position="right" :min="0" :max="100" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="巡检状态" prop="checkStatus">
          <el-radio-group v-model="form.checkStatus">
            <el-radio
              v-for="item in checkStatusOptions"
              :key="item.value"
              :label="item.value"
            >{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="巡检人" prop="checkUser">
          <el-input v-model="form.checkUser" placeholder="请输入巡检人" />
        </el-form-item>
        <el-form-item label="问题描述" prop="problemDesc">
          <el-input v-model="form.problemDesc" type="textarea" placeholder="请输入问题描述" />
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
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/ops/record"
import { optionselectServer } from "@/api/ops/server"

export default {
  name: "OpsRecord",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      recordList: [],
      serverOptions: [],
      title: "",
      open: false,
      checkStatusOptions: [
        { label: "正常", value: "0" },
        { label: "异常", value: "1" }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serverId: undefined,
        serverName: undefined,
        checkStatus: undefined,
        checkUser: undefined
      },
      form: {},
      rules: {
        serverId: [
          { required: true, message: "服务器不能为空", trigger: "change" }
        ],
        checkTime: [
          { required: true, message: "巡检时间不能为空", trigger: "change" }
        ],
        checkStatus: [
          { required: true, message: "巡检状态不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getServerOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getServerOptions() {
      optionselectServer().then(response => {
        this.serverOptions = response.data
      })
    },
    checkStatusLabel(value) {
      const item = this.checkStatusOptions.find(item => item.value === value)
      return item ? item.label : value
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        recordId: undefined,
        serverId: undefined,
        checkTime: undefined,
        cpuUsage: 0,
        memoryUsage: 0,
        diskUsage: 0,
        checkStatus: "0",
        problemDesc: undefined,
        checkUser: undefined,
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.getServerOptions()
      this.open = true
      this.title = "添加巡检记录"
    },
    handleUpdate(row) {
      this.reset()
      this.getServerOptions()
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改巡检记录"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != undefined) {
            updateRecord(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecord(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const recordIds = row.recordId || this.ids
      this.$modal.confirm('是否确认删除巡检记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('ops/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.form-number,
.form-select {
  width: 100%;
}
</style>
