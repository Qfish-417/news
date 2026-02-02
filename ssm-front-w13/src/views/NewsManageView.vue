<template>
  <!--新增，查询工具条-->
  <el-row :gutter="20" class="el-row" type="flex" style="margin:5px 0 5px 0">
    <el-col :span="15" class="el-col">
      <el-button type="primary" @click="handleAdd">新增新闻</el-button>
      <el-button type="success" @click="handleGenerateIndex" v-if="userType === 'admin'">生成首页</el-button>
    </el-col>
    <el-col :span="3" class="el-col">
      <el-select v-model="queryStatus" placeholder="状态筛选" clearable @change="handleQuery">
        <el-option label="全部" value=""></el-option>
        <el-option label="草稿" value="draft"></el-option>
        <el-option label="已提交" value="submitted"></el-option>
        <el-option label="已发布" value="published"></el-option>
        <el-option label="已退回" value="returned"></el-option>
      </el-select>
    </el-col>
    <el-col :span="3" class="el-col">
      <el-select v-model="queryCategory" placeholder="栏目筛选" clearable @change="handleQuery">
        <el-option label="全部" value=""></el-option>
        <el-option label="时政新闻" value="时政新闻"></el-option>
        <el-option label="社会新闻" value="社会新闻"></el-option>
        <el-option label="科技新闻" value="科技新闻"></el-option>
        <el-option label="体育新闻" value="体育新闻"></el-option>
        <el-option label="娱乐新闻" value="娱乐新闻"></el-option>
      </el-select>
    </el-col>
    <el-col :span="3" class="el-col">
      <el-input type="text" v-model="queryText" placeholder="标题/内容" />
    </el-col>
    <el-col :span="3" class="el-col">
      <el-button type="warning" @click="handleQuery">查询</el-button>
    </el-col>
  </el-row>
  <!--数据表格开始-->
  <el-row :gutter="20" class="el-row" type="flex">
    <el-table :data="tableData" stripe>
      <el-table-column label="序号" prop="newsId" width="60px">
      </el-table-column>
      <el-table-column label="新闻标题" prop="newsTitle" width="200px" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="栏目" prop="newsCategory" width="100px">
      </el-table-column>
      <el-table-column label="发布人" prop="publisherName" width="100px">
      </el-table-column>
      <el-table-column label="部门" prop="deptName" width="100px">
      </el-table-column>
      <el-table-column label="状态" prop="newsStatus" width="100px">
        <template #default="scope">
          <el-tag v-if="scope.row.newsStatus === 'draft'" type="info">草稿</el-tag>
          <el-tag v-else-if="scope.row.newsStatus === 'submitted'" type="warning">已提交</el-tag>
          <el-tag v-else-if="scope.row.newsStatus === 'published'" type="success">已发布</el-tag>
          <el-tag v-else-if="scope.row.newsStatus === 'returned'" type="danger">已退回</el-tag>
          <el-tag v-else>{{ scope.row.newsStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" prop="publishTime" width="160px">
      </el-table-column>
      <el-table-column label="操作" width="300px" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleView(scope.$index, scope.row)">查看</el-button>
          <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)" 
            v-if="canEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="warning" @click="handleSubmit(scope.$index, scope.row)" 
            v-if="scope.row.newsStatus === 'draft' && scope.row.publisherId === userId">提交</el-button>
          <el-button size="small" type="info" @click="handleWithdraw(scope.$index, scope.row)" 
            v-if="scope.row.newsStatus === 'submitted' && scope.row.publisherId === userId">撤回</el-button>
          <el-button size="small" type="success" @click="handleReview(scope.$index, scope.row)" 
            v-if="scope.row.newsStatus === 'submitted' && userType === 'admin'">审核</el-button>
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
  
  <!--审核对话框-->
  <el-dialog title="审核新闻" width="500px" v-model="dlgReviewVisible">
    <el-form :model="reviewForm" :rules="reviewRules" ref="reviewFormRef" :label-width="100">
      <el-form-item label="新闻标题">
        <span>{{ reviewForm.newsTitle }}</span>
      </el-form-item>
      <el-form-item label="审核操作" prop="action">
        <el-radio-group v-model="reviewForm.action">
          <el-radio label="publish">发布</el-radio>
          <el-radio label="return">退回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="退回原因" prop="returnReason" v-if="reviewForm.action === 'return'">
        <el-input type="textarea" v-model="reviewForm.returnReason" placeholder="请输入退回原因" :rows="4"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="closeReview">取 消</el-button>
        <el-button type="primary" @click="submitReview">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listNews, deleteNews, submitNews, withdrawNews, reviewNews, generateNewsIndex } from "../api/index";
import { useLoginStore } from '../stores/login';
import { useRouter } from 'vue-router';

const loginStore = useLoginStore()
const router = useRouter()

const userType = computed(() => loginStore.userType || 'reporter')
const userId = computed(() => loginStore.userId)

// 数据查询字段
const queryText = ref('')
const queryStatus = ref('')
const queryCategory = ref('')
// 表格数据
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 审核对话框
const dlgReviewVisible = ref(false)
const reviewFormRef = ref(null)
const reviewForm = reactive({
  newsId: null,
  newsTitle: '',
  action: 'publish',
  returnReason: ''
})
const reviewRules = {
  action: [
    { required: true, message: '请选择审核操作', trigger: 'change' }
  ],
  returnReason: [
    { required: true, message: '请输入退回原因', trigger: 'blur', validator: (rule, value, callback) => {
      if (reviewForm.action === 'return' && !value) {
        callback(new Error('退回时必须填写退回原因'))
      } else {
        callback()
      }
    }}
  ]
}

// 权限判断
const canEdit = (row) => {
  if (userType.value === 'admin') return true
  return row.newsStatus === 'draft' && row.publisherId === userId.value
}

const canDelete = (row) => {
  if (userType.value === 'admin') return true
  return row.newsStatus === 'draft' && row.publisherId === userId.value
}

// 装载函数中初始化获取数据
onMounted(() => {
  getTableData()
});

// 获取表格数据
const getTableData = () => {
  const params = {}
  if (queryStatus.value) params.status = queryStatus.value
  if (queryCategory.value) params.category = queryCategory.value
  
  // 如果是记者，只查询自己的新闻
  if (userType.value === 'reporter') {
    params.publisherId = userId.value
  }
  
  let postData = {
    queryText: queryText.value,
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    start: (currentPage.value - 1) * pageSize.value
  };
  
  listNews(postData, params)
    .then((res) => {
      tableData.value = (res.listTable || []).map(item => ({
        ...item
      }))
      total.value = res.total
    })
    .catch((rej) => {
      console.log(rej, "数据表格请求错误");
      ElMessage.error('获取新闻列表失败')
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
  router.push('/news/edit')
}

// 查看触发函数
const handleView = (index, row) => {
  router.push(`/news/detail/${row.newsId}`)
}

// 编辑触发函数
const handleEdit = (index, row) => {
  router.push(`/news/edit/${row.newsId}`)
}

// 提交新闻
const handleSubmit = (index, row) => {
  ElMessageBox.confirm('确认提交该新闻进行审核？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    submitNews({ newsId: row.newsId })
      .then((res) => {
        ElMessage.success('提交成功');
        getTableData();
      })
      .catch((rej) => {
        ElMessage.error('提交失败：' + (rej.message || '未知错误'));
      });
  }).catch(() => {});
}

// 撤回提交
const handleWithdraw = (index, row) => {
  ElMessageBox.confirm('确认撤回该新闻？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    withdrawNews({ newsId: row.newsId })
      .then((res) => {
        ElMessage.success('撤回成功');
        getTableData();
      })
      .catch((rej) => {
        ElMessage.error('撤回失败：' + (rej.message || '未知错误'));
      });
  }).catch(() => {});
}

// 审核新闻
const handleReview = (index, row) => {
  reviewForm.newsId = row.newsId
  reviewForm.newsTitle = row.newsTitle
  reviewForm.action = 'publish'
  reviewForm.returnReason = ''
  dlgReviewVisible.value = true
}

// 提交审核
const submitReview = () => {
  reviewFormRef.value.validate((valid) => {
    if (valid) {
      reviewNews({
        newsId: reviewForm.newsId,
        action: reviewForm.action,
        returnReason: reviewForm.returnReason
      })
        .then((res) => {
          ElMessage.success('审核成功');
          dlgReviewVisible.value = false;
          getTableData();
        })
        .catch((rej) => {
          ElMessage.error('审核失败：' + (rej.message || '未知错误'));
        });
    }
  });
}

// 关闭审核对话框
const closeReview = () => {
  reviewFormRef.value.resetFields();
  dlgReviewVisible.value = false;
}

// 删除触发函数
const handleDelete = (index, row) => {
  ElMessageBox.confirm('此操作将永久删除该新闻, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteNews({ newsId: row.newsId })
      .then((res) => {
        ElMessage.success('删除成功');
        getTableData();
      })
      .catch((rej) => {
        ElMessage.error('删除失败：' + (rej.message || '未知错误'));
      });
  }).catch(() => {});
}

// 生成首页
const handleGenerateIndex = () => {
  ElMessageBox.confirm('确认生成新闻首页静态页面？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    generateNewsIndex()
      .then((res) => {
        ElMessage.success('生成成功：' + (res.objResult || ''));
      })
      .catch((rej) => {
        ElMessage.error('生成失败：' + (rej.message || '未知错误'));
      });
  }).catch(() => {});
}

</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
</style>

