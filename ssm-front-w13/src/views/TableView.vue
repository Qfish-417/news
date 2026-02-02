<template>
  <!--新增，查询工具条-->
  <el-row :gutter="20" class="el-row" type="flex" style="margin:5px 0 5px 0">
    <el-col :span="15" class="el-col">
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </el-col>
    <el-col :span="6" class="el-col">
      <el-input type="text" v-model="queryText" placeholder="用户账号/用户姓名" />
    </el-col>
    <el-col :span="3" class="el-col">
      <el-button type="warning" @click="handleQuery">查询</el-button>
    </el-col>
  </el-row>
  <!--数据表格开始-->
  <el-row :gutter="20" class="el-row" type="flex">
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column label="序号" prop="userId" width="60px">
      </el-table-column>
      <el-table-column label="用户账号" prop="loginCode">
      </el-table-column>
      <el-table-column label="用户姓名" prop="userName">
      </el-table-column>
      <el-table-column label="用户头像" width="120px">
        <template #default="scope">
          <el-avatar :size="40" :src="resolveImage(scope.row.userImage)">
            {{ scope.row.userName ? scope.row.userName.substring(0, 1).toUpperCase() : 'U' }}
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="手机号码" prop="userPhone">
      </el-table-column>
      <el-table-column label="电子邮箱" prop="userEmail" width="160px">
      </el-table-column>
      <el-table-column label="用户类型" prop="userType">
        <template #default="scope">
          <el-tag :type="scope.row.userType === 'admin' ? 'danger' : 'success'">
            {{ scope.row.userType === 'admin' ? '管理员' : '记者' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280px">
        <template #default="scope">
          <el-button size="small" type="info" @click="handleUpload(scope.$index, scope.row)">头像</el-button>
          <el-button size="small" type="warning" @click="handleChangePassword(scope.$index, scope.row)"
            v-if="canChangePassword(scope.row)">密码</el-button>
          <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)" 
            v-if="canEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)" 
            v-if="canDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-row>
  <!--数据表格结束-->
  <!--数据分页开始-->
  <el-row>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
      :page-size="pageSize" :page-sizes="[5, 10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </el-row>
  <!--数据分页结束-->
  
  <!--新增编辑对话框-->
  <el-dialog :title="opFlag === 'add' ? '新增用户' : '编辑用户'" width="500px" v-model="dlgFormVisible" @close="closeForm">
    <el-form :model="dlgForm" :rules="rules" ref="dlgFormRef" :label-width="formLabelWidth">
      <el-input type="hidden" v-model="dlgForm.userId"></el-input>
      <el-form-item label="用户账号" prop="loginCode">
        <el-input v-model="dlgForm.loginCode" placeholder="请输入用户账号" :readonly="isRead"></el-input>
      </el-form-item>
      <el-form-item label="用户姓名" prop="userName">
        <el-input v-model="dlgForm.userName" placeholder="请输入用户姓名"></el-input>
      </el-form-item>
      <el-form-item label="手机号码" prop="userPhone">
        <el-input v-model="dlgForm.userPhone" placeholder="请输入手机号码"></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱" prop="userEmail">
        <el-input v-model="dlgForm.userEmail" placeholder="请输入电子邮箱"></el-input>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType" v-if="opFlag === 'add' && loginStore.userType === 'admin'">
        <el-select v-model="dlgForm.userType" placeholder="请选择用户类型">
          <el-option label="管理员" value="admin"></el-option>
          <el-option label="记者" value="reporter"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="部门" prop="deptId" v-if="loginStore.userType === 'admin'">
        <el-select v-model="dlgForm.deptId" placeholder="请选择部门">
          <el-option v-for="dept in deptList" :key="dept.deptId" :label="dept.deptName" :value="dept.deptId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="closeForm">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  
  <!--修改密码对话框-->
  <el-dialog title="修改密码" width="400px" v-model="dlgChangePasswordVisible" @close="closePasswordForm">
    <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" :label-width="formLabelWidth">
      <el-form-item label="原密码" prop="oldPassword" v-if="!isAdminChange">
        <el-input type="password" v-model="passwordForm.oldPassword" placeholder="请输入原密码" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码（至少6位）" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input type="password" v-model="passwordForm.confirmPassword" placeholder="请再次输入新密码" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="closePasswordForm">取 消</el-button>
        <el-button type="primary" @click="submitPasswordForm">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  
  <!--图片上传对话框-->
  <el-dialog title="头像上传" width="400px" v-model="dlgUploadVisible" @close="closeUpload">
    <el-form :model="uploadForm" :label-width="formLabelWidth">
      <el-form-item label="当前账号">
        <span>{{ uploadingUser ? uploadingUser.userName : '未选择' }}</span>
      </el-form-item>
      <el-form-item>
        <div class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        <el-upload 
          class="upload-demo" 
          :on-change="getFile" 
          :file-list="fileList" 
          :auto-upload="false" 
          :limit="1" 
          accept="image/png,image/jpeg,image/jpg,image/bmp"
          :on-exceed="handleExceed"
        >
          <el-button type="primary">选择文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="头像预览">
        <div v-if="imgUrl" style="margin-top: 10px;">
          <img :src="imgUrl" alt="头像预览" width="120" height="120" style="border-radius: 50%; object-fit: cover; border: 1px solid #dcdfe6;" />
        </div>
        <div v-else style="color:#999; margin-top: 10px;">请选择图片文件</div>
      </el-form-item>
      <el-form-item>
        <el-button @click="closeUpload">取 消</el-button>
        <el-button type="primary" @click="uploadSubmit" :loading="uploading">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUser, addUser, editUser, delUser } from "../api/index"
import { useLoginStore } from '../stores/login'
import axios from "axios"

const loginStore = useLoginStore()

// 数据查询字段
const queryText = ref('')
// 表格数据
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)
const uploadingUser = ref(null)
const deptList = ref([
  { deptId: 1, deptName: '技术部' },
  { deptId: 2, deptName: '新闻部' },
  { deptId: 3, deptName: '编辑部' }
])

// 新增编辑功能变量
const dlgFormVisible = ref(false)
const formLabelWidth = ref('100px')
const opFlag = ref('add')
const isRead = ref(false)
const dlgForm = reactive({
  userId: 0,
  loginCode: '',
  userName: '',
  userPhone: '',
  userEmail: '',
  userType: 'reporter',
  deptId: null
})

// 通过引用方式获取表单对象
const dlgFormRef = ref(null)
// 表单合法性验证规则，与控件关联
const rules = {
  loginCode: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  userEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  userPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 修改密码相关变量
const dlgChangePasswordVisible = ref(false)
const passwordFormRef = ref(null)
const isAdminChange = ref(false)
const changingUserId = ref(null)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 修改密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 图片上传
const dlgUploadVisible = ref(false)
const fileList = ref([])
const uploadRef = ref(null)
const imgUrl = ref('')
const uploading = ref(false)
const uploadForm = reactive({
  userId: null,
  imagePath: ''
})

const resolveImage = (path) => {
  if (!path) {
    return ''
  }
  // 如果路径已经是完整URL，直接返回
  if (path.startsWith('http') || path.startsWith('data:')) {
    return path
  }
  // 否则添加基础路径
  const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
  return baseUrl + '/' + path.replace(/^\/+/, '')
}

// 权限判断
const canEdit = (row) => {
  if (loginStore.userType === 'admin') return true
  return row.userId === loginStore.userId
}

const canDelete = (row) => {
  // 记者不能删除用户，也不能删除自己
  if (loginStore.userType === 'reporter') return false
  // 管理员不能删除自己
  if (row.userId === loginStore.userId) return false
  return true
}

const canChangePassword = (row) => {
  if (loginStore.userType === 'admin') return true
  return row.userId === loginStore.userId
}

// 装载函数中初始化获取数据
onMounted(() => {
  getTableData()
})

// 获取表格数据
const getTableData = () => {
  let postData = {
    queryText: queryText.value,
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    start: (currentPage.value - 1) * pageSize.value
  };
  
  console.log('请求参数:', postData);
  
  listUser(postData)
    .then((res) => {
      console.log('响应数据:', res);
      
      // 根据实际情况处理
      let data = [];
      let totalCount = 0;
      
      // 情况1：直接包含 listTable 和 total
      if (res && res.listTable !== undefined) {
        data = Array.isArray(res.listTable) ? res.listTable : [];
        totalCount = res.total || 0;
      }
      // 情况2：包装在 objResult 中
      else if (res && res.msgResult === 'success' && res.objResult) {
        const objResult = res.objResult;
        if (objResult.listTable !== undefined) {
          data = Array.isArray(objResult.listTable) ? objResult.listTable : [];
          totalCount = objResult.total || 0;
        } else if (objResult.list !== undefined) {
          data = Array.isArray(objResult.list) ? objResult.list : [];
          totalCount = objResult.total || 0;
        }
      }
      // 情况3：直接是数组
      else if (Array.isArray(res)) {
        data = res;
        totalCount = res.length;
      }
      
      console.log('解析后的数据:', data.length, '条');
      console.log('解析后的总数:', totalCount);
      
      // 如果是记者，过滤掉管理员
      if (loginStore.userType === 'reporter') {
        const beforeFilter = data.length;
        data = data.filter(item => item && item.userType !== 'admin');
        console.log(`记者过滤: ${beforeFilter} -> ${data.length} 条`);
      }
      
      tableData.value = data;
      total.value = totalCount;
      
      if (data.length === 0) {
        ElMessage.info('暂无数据');
      }
    })
    .catch((error) => {
      console.error('请求错误:', error);
      ElMessage.error('获取数据失败');
      tableData.value = [];
      total.value = 0;
    });
}
// 分页大小改变
const handleSizeChange = (size) => {
  pageSize.value = size;
  currentPage.value = 1;
  getTableData();
}

// 当前页改变
const handleCurrentChange = (page) => {
  currentPage.value = page;
  getTableData();
}

// 数据查询
const handleQuery = () => {
  currentPage.value = 1;
  getTableData();
}

// 新增触发函数
const handleAdd = () => {
  // 记者不能新增用户
  if (loginStore.userType === 'reporter') {
    ElMessage.warning('记者无权限新增用户')
    return
  }
  dlgFormVisible.value = true;
  opFlag.value = 'add';
  isRead.value = false;
  dlgForm.userId = 0;
  dlgForm.loginCode = "";
  dlgForm.userName = "";
  dlgForm.userEmail = "";
  dlgForm.userPhone = "";
  dlgForm.userType = "reporter";
  dlgForm.deptId = null;
  
  // 重置验证状态
  if (dlgFormRef.value) {
    dlgFormRef.value.resetFields()
  }
}

// 编辑触发函数
const handleEdit = (index, row) => {
  dlgFormVisible.value = true;
  opFlag.value = 'edit';
  isRead.value = true;
  dlgForm.userId = row.userId;
  dlgForm.loginCode = row.loginCode;
  dlgForm.userName = row.userName;
  dlgForm.userEmail = row.userEmail;
  dlgForm.userPhone = row.userPhone;
  dlgForm.userType = row.userType;
  dlgForm.deptId = row.deptId;
  
  // 重置验证状态
  if (dlgFormRef.value) {
    dlgFormRef.value.clearValidate()
  }
}

// 新增编辑提交
const submitForm = () => {
  dlgFormRef.value.validate((valid) => {
    if (valid) {
      let postData = new FormData();
      postData.append('userId', dlgForm.userId);
      postData.append('loginCode', dlgForm.loginCode);
      postData.append('userName', dlgForm.userName);
      postData.append('userEmail', dlgForm.userEmail);
      postData.append('userPhone', dlgForm.userPhone);
      if (opFlag.value === 'add') {
        postData.append('userType', dlgForm.userType);
      }
      if (dlgForm.deptId) {
        postData.append('deptId', dlgForm.deptId);
      }
      
      dlgFormVisible.value = false;
      
      if (opFlag.value === 'add') {
        addUser(postData)
          .then((res) => {
            if (res.msgResult === 'success') {
              ElMessage.success('用户添加成功');
              getTableData();
            } else {
              ElMessage.error(res.objResult || '用户添加失败');
            }
          })
          .catch((error) => {
            console.error(error);
            ElMessage.error('用户添加失败');
          });
      } else if (opFlag.value === 'edit') {
        editUser(postData)
          .then((res) => {
            if (res.msgResult === 'success') {
              ElMessage.success('用户更新成功');
              getTableData();
            } else {
              ElMessage.error(res.objResult || '用户更新失败');
            }
          })
          .catch((error) => {
            console.error(error);
            ElMessage.error('用户更新失败');
          });
      }
    }
  });
}

// 删除触发函数
const handleDelete = (index, row) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.userName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let postData = new FormData();
    postData.append('userId', row.userId);
    delUser(postData)
      .then((res) => {
        if (res.msgResult === 'success') {
          ElMessage.success('用户删除成功');
          getTableData();
        } else {
          ElMessage.error(res.objResult || '用户删除失败');
        }
      })
      .catch((error) => {
        console.error(error);
        ElMessage.error('用户删除失败');
      });
  }).catch(() => {
    // 用户取消
  });
}

// 关闭新增编辑对话框
const closeForm = () => {
  if (dlgFormRef.value) {
    dlgFormRef.value.resetFields();
  }
  dlgFormVisible.value = false;
}

// 修改密码按钮点击事件
const handleChangePassword = (index, row) => {
  dlgChangePasswordVisible.value = true
  changingUserId.value = row.userId
  isAdminChange.value = loginStore.userType === 'admin' && row.userId !== loginStore.userId
  
  // 重置表单
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  
  // 如果是管理员重置其他用户密码，不需要原密码验证
  if (isAdminChange.value) {
    // 移除原密码的必填验证
    if (passwordRules.oldPassword) {
      passwordRules.oldPassword[0].required = false
    }
  } else {
    if (passwordRules.oldPassword) {
      passwordRules.oldPassword[0].required = true
    }
  }
  
  // 重置验证状态
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate()
  }
}

// 提交修改密码表单
const submitPasswordForm = () => {
  passwordFormRef.value.validate((valid) => {
    if (valid) {
      // 验证新密码和确认密码是否一致
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        ElMessage.error('两次输入的密码不一致');
        return;
      }
      
      // 构造请求数据
      let requestData = {
        userId: changingUserId.value,
        newPassword: passwordForm.newPassword
      };
      
      // 如果不是管理员修改其他用户密码，需要原密码
      if (!isAdminChange.value) {
        requestData.oldPassword = passwordForm.oldPassword;
      }
      
      console.log('修改密码请求:', requestData);
      
      axios.post('/api/user/changePassword', requestData, {
        headers: {
          'token': loginStore.token,
          'Content-Type': 'application/json'
        }
      })
      .then((response) => {
        const data = response.data;
        console.log('修改密码响应:', data);
        
        if (data && data.msgResult === 'success') {
          ElMessage.success(data.objResult || '密码修改成功');
          closePasswordForm();
        } else {
          const errorMsg = data?.objResult || '密码修改失败';
          ElMessage.error(errorMsg);
        }
      })
      .catch((error) => {
        console.error('修改密码请求失败:', error);
        
        if (error.response) {
          // 服务器返回了错误状态码
          const status = error.response.status;
          const errorData = error.response.data;
          
          console.error('错误响应:', errorData);
          
          if (status === 401) {
            ElMessage.error('登录已过期，请重新登录');
          } else if (status === 403) {
            ElMessage.error('无权限操作');
          } else if (errorData && errorData.objResult) {
            ElMessage.error(errorData.objResult);
          } else {
            ElMessage.error(`密码修改失败 (${status})`);
          }
        } else if (error.request) {
          ElMessage.error('网络请求失败，请检查网络连接');
        } else {
          ElMessage.error('请求配置错误: ' + error.message);
        }
      });
    } else {
      console.log('表单验证失败');
      return false;
    }
  });
}

// 关闭修改密码对话框
const closePasswordForm = () => {
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields()
  }
  dlgChangePasswordVisible.value = false
}

// 图片上传按钮
const handleUpload = (index, row) => {
  dlgUploadVisible.value = true;
  uploadingUser.value = row;
  fileList.value = [];
  uploadForm.userId = row.userId;
  imgUrl.value = resolveImage(row.userImage);
  uploading.value = false;
}

// 获取上传图片
const getFile = (file, fileList) => {
  // 限制文件大小500KB
  if (file.size / 1024 > 500) {
    ElMessage.warning('图片大小不能超过500KB')
    return false
  }
  
  // 限制文件类型
  const isImage = file.raw.type.startsWith('image/')
  if (!isImage) {
    ElMessage.warning('只能上传图片文件')
    return false
  }
  
  // 保存文件
  fileList.value = [file]
  uploadForm.file = file.raw
  
  // 预览图片
  const reader = new FileReader();
  reader.readAsDataURL(file.raw);
  reader.onload = (e) => {
    imgUrl.value = e.target.result;
  };
}

// 文件超出限制
const handleExceed = (files, fileList) => {
  ElMessage.warning('只能上传一个文件')
}

// 图片上传
const uploadSubmit = () => {
  if (!uploadingUser.value) {
    ElMessage.warning('请先选择要更新头像的用户');
    return;
  }
  if (!uploadForm.file) {
    ElMessage.warning('请选择要上传的图片');
    return;
  }
  
  uploading.value = true;
  
  let postData = new FormData();
  postData.append('file', uploadForm.file);
  
  axios.post('/api/upload/image', postData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'token': loginStore.token
    }
  }).then((response) => {
    if (response.data.msgResult === "success") {
      const imagePath = response.data.objResult;
      
      // 更新用户头像
      axios.post('/api/user/avatar', {
        userId: uploadingUser.value.userId,
        userImage: imagePath
      }, {
        headers: {
          'token': loginStore.token,
          'Content-Type': 'application/json'
        }
      }).then((res) => {
        if (res.data.msgResult === 'success') {
          ElMessage.success('头像更新成功');
          
          // 更新表格中的头像
          const index = tableData.value.findIndex(u => u.userId === uploadingUser.value.userId)
          if (index !== -1) {
            tableData.value[index].userImage = imagePath
          }
          
          // 如果更新的是当前登录用户的头像，更新store
          if (uploadingUser.value.userId === loginStore.userId) {
            loginStore.setAvatar(imagePath)
          }
          
          closeUpload()
        } else {
          ElMessage.error('头像更新失败')
        }
      }).catch((error) => {
        console.error(error)
        ElMessage.error('头像更新失败')
      })
    } else {
      ElMessage.error('图片上传失败')
    }
  }).catch((error) => {
    console.error(error)
    ElMessage.error('图片上传失败')
  }).finally(() => {
    uploading.value = false
  })
}

// 关闭图片上传对话框
const closeUpload = () => {
  fileList.value = [];
  uploadForm.file = null;
  dlgUploadVisible.value = false;
  uploadingUser.value = null;
  uploading.value = false;
}

</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
.el-col {
  padding-left: 0 !important;
  padding-right: 0 !important;
}
</style>