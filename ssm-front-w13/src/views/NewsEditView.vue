<template>
  <div class="news-edit-container">
    <el-scrollbar class="news-edit-scrollbar" height="calc(100vh - 100px)">
      <div class="news-edit-content">
        <el-form :model="newsForm" :rules="rules" ref="newsFormRef" :label-width="100">
          <el-form-item label="新闻标题" prop="newsTitle">
            <el-input v-model="newsForm.newsTitle" placeholder="请输入新闻标题" style="width: 600px;"></el-input>
          </el-form-item>
          
          <el-form-item label="新闻栏目" prop="newsCategory">
            <el-select v-model="newsForm.newsCategory" placeholder="请选择栏目" style="width: 200px;">
              <el-option label="时政新闻" value="时政新闻"></el-option>
              <el-option label="社会新闻" value="社会新闻"></el-option>
              <el-option label="科技新闻" value="科技新闻"></el-option>
              <el-option label="体育新闻" value="体育新闻"></el-option>
              <el-option label="娱乐新闻" value="娱乐新闻"></el-option>
              <el-option label="财经新闻" value="财经新闻"></el-option>
              <el-option label="教育新闻" value="教育新闻"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="新闻正文" prop="newsContent">
            <div class="editor-container">
              <div class="editor-wrapper">
                <QuillEditor 
                  v-model:content="newsForm.newsContent" 
                  :options="editorOptions"
                  content-type="html"
                  @ready="onEditorReady"
                  class="quill-editor"
                />
              </div>
              <div class="editor-hint">
                提示：支持富文本编辑，可以直接拖拽图片或使用工具栏上传
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="新闻图片">
            <div class="image-manager">
              <el-upload
                class="upload-area"
                :action="uploadUrl"
                :on-success="handleImageSuccess"
                :before-upload="beforeImageUpload"
                :show-file-list="false"
                :headers="uploadHeaders"
                multiple
              >
                <el-button type="primary" size="small">上传图片</el-button>
                <template #tip>
                  <div class="el-upload__tip">支持jpg/png格式，单个文件不超过5MB</div>
                </template>
              </el-upload>
              
              <div class="image-list" v-if="newsForm.newsImages && newsForm.newsImages.length > 0">
                <div class="image-item" v-for="(image, index) in newsForm.newsImages" :key="index">
                  <div class="image-wrapper">
                    <img :src="resolveImage(image.url)" :alt="image.name" class="image-thumbnail">
                    <div class="image-actions">
                      <el-button size="small" @click="insertImageAtCursor(image.url)" type="text">
                        <el-icon><Position /></el-icon>
                        插入到光标位置
                      </el-button>
                      <el-button size="small" @click="removeImage(index)" type="text" style="color: #f56c6c;">
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </div>
                    <div class="image-info">
                      {{ image.name }}
                      <span v-if="image.size">({{ formatFileSize(image.size) }})</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="新闻附件">
            <div class="attachment-manager">
              <el-upload
                class="upload-area"
                :action="attachmentUploadUrl"
                :on-success="handleAttachmentSuccess"
                :before-upload="beforeAttachmentUpload"
                :show-file-list="false"
                :headers="uploadHeaders"
                multiple
              >
                <el-button type="primary" size="small">上传附件</el-button>
                <template #tip>
                  <div class="el-upload__tip">支持各种文件格式，单个文件不超过10MB</div>
                </template>
              </el-upload>
              
              <div class="attachment-list" v-if="newsForm.newsAttachments && newsForm.newsAttachments.length > 0">
                <div class="attachment-item" v-for="(attachment, index) in newsForm.newsAttachments" :key="index">
                  <div class="attachment-wrapper">
                    <div class="attachment-name">
                      <el-icon><Document /></el-icon>
                      <span class="file-name">{{ getAttachmentDisplayName(attachment) }}</span>
                    </div>
                    <div class="attachment-actions">
                      <el-button 
                        size="small" 
                        @click="downloadAttachment(attachment)" 
                        type="text"
                        :loading="isDownloading(attachment)">
                        <el-icon><Download /></el-icon>
                        {{ isDownloading(attachment) ? '下载中...' : '下载' }}
                      </el-button>
                      <el-button 
                        size="small" 
                        @click="previewAttachment(attachment)" 
                        type="text"
                        v-if="isPreviewable(attachment)">
                        <el-icon><View /></el-icon>
                        预览
                      </el-button>
                      <el-button 
                        size="small" 
                        @click="insertAttachmentAtCursor(attachment.url || attachment, getAttachmentDisplayName(attachment))" 
                        type="text">
                        <el-icon><Position /></el-icon>
                        插入到正文
                      </el-button>
                      <el-button 
                        size="small" 
                        @click="removeAttachment(index)" 
                        type="text" 
                        style="color: #f56c6c;">
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </div>
                    <div class="attachment-info">
                      <span v-if="attachment.size">{{ formatFileSize(attachment.size) }}</span>
                      <span v-else-if="attachment.name">{{ getAttachmentDisplayName(attachment) }}</span>
                      <span v-else>已上传</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="封面图片">
            <div class="cover-image-section">
              <el-upload
                class="cover-upload"
                :action="uploadUrl"
                :on-success="handleCoverImageSuccess"
                :before-upload="beforeImageUpload"
                :show-file-list="false"
                :headers="uploadHeaders"
              >
                <el-button size="small" type="primary">选择封面图片</el-button>
                <template #tip>
                  <div class="el-upload__tip">建议尺寸：1200×600px，将作为新闻列表的展示图片</div>
                </template>
              </el-upload>
              
              <div v-if="newsForm.coverImage" class="cover-preview">
                <img :src="resolveImage(newsForm.coverImage)" alt="封面图片" class="cover-image">
                <div class="cover-actions">
                  <el-button size="small" @click="newsForm.coverImage = ''" type="text" style="color: #f56c6c;">
                    <el-icon><Delete /></el-icon>
                    删除封面
                  </el-button>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="新闻状态" v-if="newsId">
            <el-tag v-if="newsForm.newsStatus === 'draft'" type="info">草稿</el-tag>
            <el-tag v-else-if="newsForm.newsStatus === 'submitted'" type="warning">已提交（待审核）</el-tag>
            <el-tag v-else-if="newsForm.newsStatus === 'reviewed'" type="primary">已审核（待发布）</el-tag>
            <el-tag v-else-if="newsForm.newsStatus === 'published'" type="success">已发布</el-tag>
            <el-tag v-else-if="newsForm.newsStatus === 'returned'" type="danger">已退回</el-tag>
          </el-form-item>
          
          <el-form-item label="退回原因" v-if="newsForm.newsStatus === 'returned' && newsForm.returnReason">
            <el-alert :title="newsForm.returnReason" type="error" :closable="false"></el-alert>
          </el-form-item>
          
          <el-form-item>
            <el-button @click="handleCancel">取 消</el-button>
            
            <!-- 新建新闻 -->
            <el-button type="info" @click="handleSaveDraft" v-if="!newsId">
              保存草稿
            </el-button>
            
            <el-button type="primary" @click="handleSubmit" v-if="!newsId">
              提交审核
            </el-button>
            
            <!-- 草稿状态 -->
            <el-button type="info" @click="handleSaveDraft" 
                      v-if="newsId && newsForm.newsStatus === 'draft'">
              保存草稿
            </el-button>
            
            <el-button type="primary" @click="handleSubmit" 
                      v-if="newsId && newsForm.newsStatus === 'draft'">
              提交审核
            </el-button>
            
            <!-- 已退回状态 -->
            <el-button type="primary" @click="handleUpdateAndResubmit" 
                      v-if="newsId && newsForm.newsStatus === 'returned'">
              修改后重新提交
            </el-button>
            
            <!-- 已提交、已审核、已发布状态 - 编辑会重新进入审核流程 -->
            <el-button type="primary" @click="handleUpdateAndResubmit" 
                      v-if="newsId && (newsForm.newsStatus === 'submitted' || 
                                        newsForm.newsStatus === 'reviewed' || 
                                        newsForm.newsStatus === 'published')">
              编辑（重新提交审核）
            </el-button>
          </el-form-item>
          
          <el-form-item v-if="newsId">
            <div class="status-help">
              <p><strong>状态说明：</strong></p>
              <ul>
                <li><strong>草稿</strong>：您可以保存草稿或提交审核</li>
                <li><strong>已提交</strong>：新闻已提交，等待管理员审核，您可以编辑内容（会重新进入审核流程）</li>
                <li><strong>已审核</strong>：新闻已通过审核，等待发布，您可以编辑内容（会重新进入审核流程）</li>
                <li><strong>已发布</strong>：新闻已发布，编辑后将重新提交审核</li>
                <li><strong>已退回</strong>：新闻被退回，请根据退回原因修改后重新提交</li>
              </ul>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { getNews, addNews, updateNews, submitNews } from "../api/index";
import { useLoginStore } from '../stores/login';
import { Document, Delete, Position, Download, View } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import axios from "axios";

const router = useRouter()
const route = useRoute()
const loginStore = useLoginStore()

const newsId = ref(route.params.id || null)
const newsFormRef = ref(null)
const quillEditor = ref(null)
const quillInstance = ref(null);
const downloadingAttachments = ref(new Set()); // 记录正在下载的附件

// 编辑器加载状态
const editorReady = ref(false);

// 配置上传地址
const uploadUrl = '/api/upload/image'
const attachmentUploadUrl = '/api/upload/accessory'
const uploadHeaders = {
  'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
}

const newsForm = reactive({
  newsTitle: '',
  newsCategory: '',
  newsContent: '',
  newsImages: [], // 存储所有上传的图片
  newsAttachments: [], // 存储所有附件
  coverImage: '', // 封面图片
  newsStatus: 'draft',
  returnReason: ''
})

const rules = {
  newsTitle: [
    { required: true, message: '请输入新闻标题', trigger: 'blur' },
    { min: 5, max: 200, message: '标题长度应在5-200个字符', trigger: 'blur' }
  ],
  newsCategory: [
    { required: true, message: '请选择新闻栏目', trigger: 'change' }
  ],
  newsContent: [
    { required: true, message: '请输入新闻正文', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        // 移除HTML标签后检查长度
        const text = value.replace(/<[^>]*>/g, '').trim()
        if (text.length < 10) {
          callback(new Error('正文内容至少10个字符'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 富文本编辑器配置
const editorOptions = {
  theme: 'snow',
  modules: {
    toolbar: {
      container: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ 'header': 1 }, { 'header': 2 }],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }],
        [{ 'indent': '-1'}, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        [{ 'size': ['small', false, 'large', 'huge'] }],
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['image', 'link', 'video', 'attachment'],
        ['clean']
      ],
      handlers: {
        image: () => {
          if (!editorReady.value || !quillInstance.value) {
            ElMessage.warning('编辑器尚未准备好，请稍后再试');
            return;
          }
          
          const input = document.createElement('input');
          input.setAttribute('type', 'file');
          input.setAttribute('accept', 'image/*');
          input.click();
          
          input.onchange = async () => {
            const file = input.files[0];
            if (file) {
              try {
                const formData = new FormData();
                formData.append('file', file);
                
                const token = localStorage.getItem('token') || '';
                
                const response = await axios.post('/api/upload/image', formData, {
                  headers: {
                    'token': token,
                    'Content-Type': 'multipart/form-data'
                  }
                });
                
                if (response.data && response.data.msgResult === "success") {
                  const url = response.data.objResult;
                  
                  const quill = quillInstance.value;
                  const range = quill.getSelection();
                  
                  if (range) {
                    quill.insertEmbed(range.index, 'image', resolveImage(url));
                  } else {
                    quill.insertEmbed(quill.getLength(), 'image', resolveImage(url));
                  }
                  
                  const existingIndex = newsForm.newsImages.findIndex(img => img.url === url);
                  if (existingIndex === -1) {
                    newsForm.newsImages.push({
                      url: url,
                      name: file.name,
                      size: file.size
                    });
                  }
                  
                  ElMessage.success('图片插入成功');
                } else {
                  ElMessage.error('图片上传失败: ' + (response.data?.msgResult || '未知错误'));
                }
              } catch (error) {
                console.error('上传失败:', error);
                ElMessage.error('图片上传失败: ' + error.message);
              }
            }
          }
        },
        attachment: () => {
          if (!editorReady.value || !quillInstance.value) {
            ElMessage.warning('编辑器尚未准备好，请稍后再试');
            return;
          }
          
          const input = document.createElement('input');
          input.setAttribute('type', 'file');
          input.setAttribute('multiple', 'true');
          input.click();
          
          input.onchange = async () => {
            const files = Array.from(input.files);
            if (files.length > 0) {
              for (const file of files) {
                await uploadAttachment(file);
              }
            }
          }
        }
      }
    }
  },
  placeholder: '请输入新闻正文...'
}

// 上传附件的函数
const uploadAttachment = async (file) => {
  try {
    // 检查文件大小
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
      ElMessage.error(`${file.name} 大小不能超过10MB！`);
      return;
    }
    
    const formData = new FormData();
    formData.append('file', file);
    
    const token = localStorage.getItem('token') || '';
    
    const response = await axios.post('/api/upload/accessory', formData, {
      headers: {
        'token': token,
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 处理响应
    let result = response.data;
    if (response.data && response.data.data) {
      result = response.data.data;
    }
    
    if (typeof result === 'string') {
      try {
        result = JSON.parse(result);
      } catch (e) {
        console.error('解析附件响应失败:', e);
      }
    }
    
    if (result && result.msgResult === "success") {
      const attachmentUrl = result.objResult;
      const fileName = file.name;
      
      // 添加到附件列表
      const existingIndex = newsForm.newsAttachments.findIndex(att => att.url === attachmentUrl);
      if (existingIndex === -1) {
        newsForm.newsAttachments.push({
          url: attachmentUrl,
          name: fileName,
          size: file.size
        });
      }
      
      // 插入附件链接到编辑器
      insertAttachmentLink(attachmentUrl, fileName);
      
      ElMessage.success(`附件 "${fileName}" 上传成功`);
    } else {
      const errorMsg = result?.msgResult || result?.message || '未知错误';
      ElMessage.error(`附件 "${file.name}" 上传失败: ${errorMsg}`);
    }
  } catch (error) {
    console.error('附件上传失败:', error);
    ElMessage.error(`附件 "${file.name}" 上传失败: ${error.message}`);
  }
}

// 插入附件链接到编辑器
const insertAttachmentLink = (url, fileName) => {
  if (!quillInstance.value) {
    ElMessage.warning('编辑器尚未准备好');
    return;
  }
  
  const quill = quillInstance.value;
  const range = quill.getSelection();
  
  // 解析文件路径获取下载链接
  const fileInfo = parseFilePath(url);
  let downloadUrl = url;
  if (fileInfo) {
    downloadUrl = `/api/upload/file/${fileInfo.date}/${fileInfo.filename}`;
  }
  
  // 创建附件链接HTML
  const linkHtml = `
    <div class="editor-attachment">
      <a href="${downloadUrl}" target="_blank" class="attachment-link">
        <el-icon><Document /></el-icon>
        ${fileName}
      </a>
      <span class="attachment-hint">（点击下载）</span>
    </div>
  `;
  
  if (range) {
    quill.clipboard.dangerouslyPasteHTML(range.index, linkHtml);
  } else {
    quill.clipboard.dangerouslyPasteHTML(quill.getLength(), linkHtml);
  }
}

// 修改 onEditorReady
const onEditorReady = (editor) => {
  console.log('编辑器已准备好:', editor);
  quillInstance.value = editor;
  editorReady.value = true;
};

// 图片路径处理
const resolveImage = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  
  const baseUrl = import.meta.env.VITE_API_BASE_URL || window.location.origin
  return path.startsWith('/') ? baseUrl + path : baseUrl + '/' + path
}

// 获取文件名
const getFileName = (path) => {
  if (!path) return '未命名文件'
  const parts = path.split('/')
  return parts[parts.length - 1] || path
}

// 获取附件显示名称
const getAttachmentDisplayName = (attachment) => {
  if (typeof attachment === 'string') {
    return getFileName(attachment);
  } else if (attachment.name) {
    return attachment.name;
  } else if (attachment.url) {
    return getFileName(attachment.url);
  }
  return '未命名文件';
};

// 检查附件是否正在下载
const isDownloading = (attachment) => {
  const key = getAttachmentKey(attachment);
  return downloadingAttachments.value.has(key);
};

// 获取附件唯一标识
const getAttachmentKey = (attachment) => {
  if (typeof attachment === 'string') {
    return attachment;
  } else if (attachment.url) {
    return attachment.url;
  } else if (attachment.name) {
    return attachment.name;
  }
  return JSON.stringify(attachment);
};

// 解析文件路径为下载参数
const parseFilePath = (filePath) => {
  if (!filePath) return null;
  
  // 格式可能是: file/20241223/文件名.txt
  const parts = filePath.split('/');
  
  if (parts.length >= 3) {
    const date = parts[parts.length - 2];
    const filename = parts[parts.length - 1];
    
    // 验证日期格式 (yyyyMMdd)
    const dateRegex = /^\d{8}$/;
    if (dateRegex.test(date)) {
      return {
        date: date,
        filename: filename,
        originalPath: filePath
      };
    }
  }
  
  // 如果格式不符合预期，尝试从文件路径中提取
  const filename = getFileName(filePath);
  const date = new Date().toISOString().split('T')[0].replace(/-/g, '');
  
  return {
    date: date,
    filename: filename,
    originalPath: filePath
  };
};

// 下载附件
const downloadAttachment = async (attachment) => {
  let filePath, fileName;
  
  if (typeof attachment === 'string') {
    filePath = attachment;
    fileName = getFileName(attachment);
  } else if (attachment.url) {
    filePath = attachment.url;
    fileName = attachment.name || getFileName(attachment.url);
  } else {
    ElMessage.error('无法识别的附件格式');
    return;
  }
  
  const attachmentKey = getAttachmentKey(attachment);
  
  // 检查是否正在下载
  if (downloadingAttachments.value.has(attachmentKey)) {
    ElMessage.warning('文件正在下载中，请稍候');
    return;
  }
  
  // 添加到下载队列
  downloadingAttachments.value.add(attachmentKey);
  
  try {
    // 解析文件路径
    const fileInfo = parseFilePath(filePath);
    if (!fileInfo) {
      ElMessage.error('无法解析文件路径');
      return;
    }
    
    // 使用后端下载接口
    const downloadUrl = `/api/upload/file/${fileInfo.date}/${fileInfo.filename}`;
    
    // 获取token
    const token = localStorage.getItem('token') || '';
    
    // 使用fetch下载
    const response = await fetch(downloadUrl, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'token': token
      }
    });
    
    if (!response.ok) {
      throw new Error(`下载失败: ${response.status} ${response.statusText}`);
    }
    
    // 获取blob数据
    const blob = await response.blob();
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    
    // 添加到页面并触发下载
    document.body.appendChild(link);
    link.click();
    
    // 清理
    setTimeout(() => {
      window.URL.revokeObjectURL(url);
      document.body.removeChild(link);
    }, 100);
    
    ElMessage.success('文件下载成功');
  } catch (error) {
    console.error('下载失败:', error);
    
    // 备选方案：直接打开下载链接
    try {
      const fileInfo = parseFilePath(filePath);
      if (fileInfo) {
        const downloadUrl = `/api/upload/file/${fileInfo.date}/${fileInfo.filename}`;
        window.open(downloadUrl, '_blank');
        ElMessage.info('已在新窗口打开，请右键另存为');
      }
    } catch (fallbackError) {
      ElMessage.error('下载失败，请检查网络或联系管理员');
    }
  } finally {
    // 从下载队列中移除
    downloadingAttachments.value.delete(attachmentKey);
  }
};

// 预览附件
const previewAttachment = (attachment) => {
  let filePath;
  
  if (typeof attachment === 'string') {
    filePath = attachment;
  } else if (attachment.url) {
    filePath = attachment.url;
  } else {
    ElMessage.error('无法识别的附件格式');
    return;
  }
  
  const fileInfo = parseFilePath(filePath);
  if (!fileInfo) {
    ElMessage.error('无法解析文件路径');
    return;
  }
  
  const previewUrl = `/api/upload/file/${fileInfo.date}/${fileInfo.filename}`;
  window.open(previewUrl, '_blank');
};

// 判断文件是否可预览
const isPreviewable = (attachment) => {
  let filePath;
  
  if (typeof attachment === 'string') {
    filePath = attachment;
  } else if (attachment.url) {
    filePath = attachment.url;
  } else {
    return false;
  }
  
  const fileName = getFileName(filePath).toLowerCase();
  const previewableExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.pdf', '.txt', '.md'];
  return previewableExtensions.some(ext => fileName.endsWith(ext));
};

// 插入附件链接到正文
const insertAttachmentAtCursor = (attachmentUrl, attachmentName) => {
  if (!quillInstance.value) {
    ElMessage.warning('请等待编辑器加载完成');
    return;
  }
  
  // 解析文件路径获取下载链接
  const fileInfo = parseFilePath(attachmentUrl);
  if (!fileInfo) {
    ElMessage.error('无法解析附件路径');
    return;
  }
  
  const downloadUrl = `/api/upload/file/${fileInfo.date}/${fileInfo.filename}`;
  
  insertAttachmentLink(downloadUrl, attachmentName);
};

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 图片上传处理
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB！')
    return false
  }
  return true
}

// 处理图片上传成功
const handleImageSuccess = (response, file) => {
  let result = response?.data || response;
  
  if (typeof result === 'string') {
    try {
      result = JSON.parse(result);
    } catch (e) {
      console.error('解析JSON失败:', e);
    }
  }
  
  if (!result) {
    console.error('无法解析响应');
    ElMessage.error('上传响应解析失败');
    return;
  }
  
  const isSuccess = 
    result.msgResult === 'success' || 
    result.code === 200 ||
    result.status === 'success';
  
  if (isSuccess) {
    const imageUrl = result.objResult || result.url || result.data || result.path;
    
    if (imageUrl) {
      newsForm.newsImages.push({
        url: imageUrl,
        name: file.name,
        size: file.size
      });
      
      ElMessage.success('图片上传成功');
    } else {
      ElMessage.success('上传成功，但未获取到图片地址');
    }
  } else {
    const errorMsg = result.message || result.msgResult || result.error || '未知错误';
    ElMessage.error(`上传失败: ${errorMsg}`);
  }
}

// 附件上传处理
const beforeAttachmentUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('附件大小不能超过10MB！')
    return false
  }
  return true
}

// 处理附件上传成功
const handleAttachmentSuccess = (response, file) => {
  let result = response;
  if (response && response.data) {
    result = response.data;
  }
  
  if (typeof result === 'string') {
    try {
      result = JSON.parse(result);
    } catch (e) {
      console.error('解析附件响应失败:', e);
    }
  }
  
  if (result && result.msgResult === "success") {
    const attachmentUrl = result.objResult;
    const fileName = file.name || getFileName(attachmentUrl);
    
    // 检查是否已存在
    const existingIndex = newsForm.newsAttachments.findIndex(att => 
      att.url === attachmentUrl || att.name === fileName
    );
    
    if (existingIndex === -1) {
      newsForm.newsAttachments.push({
        url: attachmentUrl,
        name: fileName,
        size: file.size,
        type: file.type
      });
    }
    
    ElMessage.success('附件上传成功');
  } else {
    const errorMsg = result?.msgResult || result?.message || '未知错误';
    ElMessage.error('附件上传失败: ' + errorMsg);
  }
};

// 封面图片上传
const handleCoverImageSuccess = (response, file) => {
  let result = response;
  if (response && response.data) {
    result = response.data;
  }
  
  if (typeof result === 'string') {
    try {
      result = JSON.parse(result);
    } catch (e) {
      console.error('解析封面响应失败:', e);
    }
  }
  
  if (result && result.msgResult === "success") {
    newsForm.coverImage = result.objResult;
    ElMessage.success('封面图片上传成功');
  } else {
    const errorMsg = result?.msgResult || result?.message || '未知错误';
    ElMessage.error('封面图片上传失败: ' + errorMsg);
  }
}

// 插入图片到光标位置
const insertImageAtCursor = (imageUrl) => {
  if (!quillInstance.value) {
    ElMessage.warning('请等待编辑器加载完成');
    return;
  }
  
  const quill = quillInstance.value;
  const range = quill.getSelection();
  if (range) {
    quill.insertEmbed(range.index, 'image', resolveImage(imageUrl));
  } else {
    quill.insertEmbed(0, 'image', resolveImage(imageUrl));
  }
}

// 移除图片
const removeImage = (index) => {
  newsForm.newsImages.splice(index, 1);
  ElMessage.success('图片已移除');
}

// 移除附件
const removeAttachment = (index) => {
  newsForm.newsAttachments.splice(index, 1);
  ElMessage.success('附件已移除');
}

// 加载新闻数据
const loadNews = async () => {
  if (!newsId.value) return;
  
  try {
    const res = await getNews(newsId.value);
    if (res.msgResult === 'success' && res.objResult) {
      const news = res.objResult;
      
      newsForm.newsTitle = news.newsTitle || '';
      newsForm.newsCategory = news.newsCategory || '';
      newsForm.newsContent = news.newsContent || '';
      newsForm.coverImage = news.coverImage || '';
      newsForm.newsStatus = news.newsStatus || 'draft';
      newsForm.returnReason = news.returnReason || '';
      
      // 解析图片列表
      if (news.newsImages) {
        try {
          newsForm.newsImages = JSON.parse(news.newsImages);
        } catch (e) {
          console.error('解析图片列表失败:', e);
          newsForm.newsImages = [];
        }
      }
      
      // 解析附件列表
      if (news.newsAttachments) {
        try {
          newsForm.newsAttachments = JSON.parse(news.newsAttachments);
        } catch (e) {
          console.error('解析附件列表失败:', e);
          newsForm.newsAttachments = [];
        }
      }
    }
  } catch (error) {
    ElMessage.error('加载新闻失败');
    console.error(error);
  }
}

// 保存草稿
const handleSaveDraft = async () => {
  try {
    await newsFormRef.value.validate();
    
    const postData = {
      ...newsForm,
      newsStatus: 'draft',
      newsImages: JSON.stringify(newsForm.newsImages),
      newsAttachments: JSON.stringify(newsForm.newsAttachments)
    };
    
    if (newsId.value) {
      postData.newsId = newsId.value;
      const res = await updateNews(postData);
      if (res.msgResult === 'success') {
        ElMessage.success('保存成功');
        router.push('/news/manage');
      } else {
        ElMessage.error('保存失败: ' + (res.objResult || '未知错误'));
      }
    } else {
      const res = await addNews(postData);
      if (res.msgResult === 'success') {
        ElMessage.success('保存成功');
        router.push('/news/manage');
      } else {
        ElMessage.error('保存失败: ' + (res.objResult || '未知错误'));
      }
    }
  } catch (error) {
    if (error.errors) {
      // 验证错误
      return;
    }
    ElMessage.error('保存失败：' + (error.message || '未知错误'));
  }
}

// 提交审核
const handleSubmit = async () => {
  try {
    await newsFormRef.value.validate();
    
    let currentNewsId = newsId.value;
    
    if (!currentNewsId) {
      // 先保存为草稿
      const postData = {
        ...newsForm,
        newsStatus: 'draft',
        newsImages: JSON.stringify(newsForm.newsImages),
        newsAttachments: JSON.stringify(newsForm.newsAttachments)
      };
      
      const res = await addNews(postData);
      if (res.msgResult === 'success' && res.objResult) {
        currentNewsId = res.objResult;
      } else {
        throw new Error('保存草稿失败');
      }
    }
    
    // 提交审核
    const submitRes = await submitNews({ newsId: currentNewsId });
    if (submitRes.msgResult === 'success') {
      ElMessage.success('提交成功，等待审核');
      router.push('/news/manage');
    } else {
      ElMessage.error('提交失败: ' + (submitRes.objResult || '未知错误'));
    }
  } catch (error) {
    if (error.errors) {
      // 验证错误
      return;
    }
    ElMessage.error('提交失败：' + (error.message || '未知错误'));
  }
}

// 更新并重新提交审核（用于已提交、已审核、已发布、已退回状态）
const handleUpdateAndResubmit = async () => {
  try {
    let confirmMessage = '';
    let successMessage = '';
    
    // 根据当前状态设置不同的提示信息
    switch (newsForm.newsStatus) {
      case 'returned':
        confirmMessage = '修改已退回的新闻将重新提交审核，确定继续吗？';
        successMessage = '修改成功，新闻已重新提交审核';
        break;
      case 'submitted':
        confirmMessage = '编辑已提交的新闻将重新进入审核流程，确定继续吗？';
        successMessage = '更新成功，新闻已重新提交审核';
        break;
      case 'reviewed':
        confirmMessage = '编辑已审核的新闻将重新提交审核，确定继续吗？';
        successMessage = '更新成功，新闻已重新提交审核';
        break;
      case 'published':
        confirmMessage = '编辑已发布的新闻将重新提交审核，确定继续吗？';
        successMessage = '更新成功，新闻已重新提交审核';
        break;
      default:
        confirmMessage = '确定要更新新闻吗？';
        successMessage = '更新成功';
    }
    
    await ElMessageBox.confirm(
      confirmMessage,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    await newsFormRef.value.validate();
    
    const postData = {
      ...newsForm,
      newsId: newsId.value,
      newsImages: JSON.stringify(newsForm.newsImages),
      newsAttachments: JSON.stringify(newsForm.newsAttachments)
    };
    
    const res = await updateNews(postData);
    
    if (res.msgResult === 'success') {
      ElMessage.success(successMessage);
      router.push('/news/manage');
    } else {
      ElMessage.error('操作失败: ' + (res.objResult || '未知错误'));
    }
  } catch (error) {
    if (error === 'cancel') {
      return;
    }
    
    if (error.errors) {
      return;
    }
    
    console.error('操作异常:', error);
    ElMessage.error('操作失败: ' + (error.message || '未知错误'));
  }
}

// 取消
const handleCancel = () => {
  router.push('/news/manage');
}

onMounted(() => {
  if (newsId.value) {
    loadNews();
  }
});

onUnmounted(() => {
  if (quillEditor.value) {
    quillEditor.value = null;
  }
});
</script>

<style scoped>
.news-edit-container {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.news-edit-scrollbar {
  flex: 1;
  overflow: hidden;
}

.news-edit-content {
  padding: 10px;
}

/* 富文本编辑器容器 */
.editor-container {
  width: 100%;
  max-width: 1200px;
}

.editor-wrapper {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
}

.quill-editor {
  min-height: 400px;
  width: 100%;
}

.quill-editor :deep(.ql-toolbar) {
  border-bottom: 1px solid #dcdfe6;
  border-top: none;
  border-left: none;
  border-right: none;
  padding: 8px;
  background: #f8f9fa;
}

.quill-editor :deep(.ql-container) {
  border: none;
  border-top: none;
  font-family: inherit;
  font-size: 14px;
}

.quill-editor :deep(.ql-editor) {
  min-height: 350px;
  padding: 20px;
  line-height: 1.6;
}

.quill-editor :deep(.ql-editor img) {
  max-width: 100%;
  height: auto;
}

.quill-editor :deep(.ql-editor .editor-attachment) {
  margin: 10px 0;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.quill-editor :deep(.ql-editor .attachment-link) {
  color: #409eff;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
}

.quill-editor :deep(.ql-editor .attachment-link:hover) {
  text-decoration: underline;
}

.quill-editor :deep(.ql-editor .attachment-hint) {
  color: #909399;
  font-size: 12px;
  margin-left: 6px;
}

.quill-editor :deep(.ql-editor a) {
  color: #409eff;
  text-decoration: none;
}

.quill-editor :deep(.ql-editor a:hover) {
  text-decoration: underline;
}

.editor-hint {
  margin-top: 10px;
  color: #909399;
  font-size: 12px;
  padding-left: 4px;
}

.image-manager,
.attachment-manager,
.cover-image-section {
  width: 100%;
}

.upload-area {
  margin-bottom: 20px;
}

.image-list,
.attachment-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-top: 10px;
}

.image-item,
.attachment-item {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 12px;
  border: 1px solid #e9ecef;
  transition: all 0.3s;
}

.image-item:hover,
.attachment-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.image-wrapper,
.attachment-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-thumbnail {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.attachment-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  margin-bottom: 10px;
}

.attachment-name {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #303133;
}

.attachment-name .el-icon {
  color: #409eff;
  font-size: 18px;
}

.file-name {
  word-break: break-all;
  flex: 1;
}

.attachment-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.attachment-info {
  font-size: 12px;
  color: #909399;
  text-align: left;
}

/* 下载按钮加载状态 */
.attachment-actions .el-button.is-loading {
  position: relative;
  pointer-events: none;
}

.attachment-actions .el-button.is-loading:before {
  content: '';
  position: absolute;
  left: -1px;
  top: -1px;
  right: -1px;
  bottom: -1px;
  border-radius: inherit;
  background-color: rgba(255, 255, 255, 0.35);
}

.image-info,
.attachment-info {
  font-size: 12px;
  color: #6c757d;
  text-align: center;
  width: 100%;
}

.cover-preview {
  margin-top: 10px;
  max-width: 400px;
}

.cover-image {
  width: 100%;
  max-height: 200px;
  object-fit: contain;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.status-help {
  background-color: #f8f9fa;
  border-radius: 6px;
  padding: 15px;
  border-left: 4px solid #409eff;
  margin-top: 10px;
}

.status-help p {
  margin-bottom: 10px;
  color: #333;
  font-weight: bold;
}

.status-help ul {
  margin: 0;
  padding-left: 20px;
  color: #666;
}

.status-help li {
  margin-bottom: 8px;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .editor-container {
    max-width: 100%;
  }
  
  .quill-editor {
    min-height: 300px;
  }
  
  .image-list,
  .attachment-list {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
}
</style>