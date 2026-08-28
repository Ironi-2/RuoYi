import request from '@/utils/request'

// 查询巡检记录列表
export function listRecord(query) {
  return request({
    url: '/ops/record/list',
    method: 'get',
    params: query
  })
}

// 查询巡检记录详细
export function getRecord(recordId) {
  return request({
    url: '/ops/record/' + recordId,
    method: 'get'
  })
}

// 新增巡检记录
export function addRecord(data) {
  return request({
    url: '/ops/record',
    method: 'post',
    data: data
  })
}

// 修改巡检记录
export function updateRecord(data) {
  return request({
    url: '/ops/record',
    method: 'put',
    data: data
  })
}

// 删除巡检记录
export function delRecord(recordId) {
  return request({
    url: '/ops/record/' + recordId,
    method: 'delete'
  })
}
