import { getInfo } from '@/api/auth'

const state = {
  username: '',
  permissions: []
}

const mutations = {
  SET_USER_INFO(state, payload) {
    state.username = payload.username
    state.permissions = payload.permissions
  }
}

const actions = {
  fetchUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        commit('SET_USER_INFO', response)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
