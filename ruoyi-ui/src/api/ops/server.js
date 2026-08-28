import request from '@/utils/request'

// 查询服务器资产列表
export function listServer(query) {
  return request({
    url: '/ops/server/list',
    method: 'get',
    params: query
  })
}

// 查询服务器资产详细
export function getServer(serverId) {
  return request({
    url: '/ops/server/' + serverId,
    method: 'get'
  })
}

// 查询服务器选择框列表
export function optionselectServer() {
  return request({
    url: '/ops/server/optionselect',
    method: 'get'
  })
}

// 新增服务器资产
export function addServer(data) {
  return request({
    url: '/ops/server',
    method: 'post',
    data: data
  })
}

// 修改服务器资产
export function updateServer(data) {
  return request({
    url: '/ops/server',
    method: 'put',
    data: data
  })
}

// 删除服务器资产
export function delServer(serverId) {
  return request({
    url: '/ops/server/' + serverId,
    method: 'delete'
  })
}
