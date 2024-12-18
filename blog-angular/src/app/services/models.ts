export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  token: string
  expires_at: string
  name: string
  email: string
  role: string
}

export interface CreateUserRequest {
  email: string
  password: string
  name: string
}

export interface CreateUserResponse {
  email: string
  name: string
}

export interface PostsResponse {
  data: PostUserView[],
  totalElements: number
  currentPageNo: number
  totalPages: number
  hasNextPage: boolean
  hasPreviousPage: boolean
}

export interface PostUserView {
  id: number
  title: string
  slug: string
  content: string
  createdByUserName: string
  createdAt: Date
  comments: Comment[]
}

export interface Comment {
  id: number
  name: string
  email: string
  content: string
  createdAt: Date
}


export interface User {
  id: number
  name: string
  email: string
  role: string
}

export interface CreatePostPayload {
  title: string
  slug: string
  content: string
}

export interface UpdatePostPayload {
  title: string
  slug: string
  content: string
}

export interface CreateCommentPayload {
  name: string
  email: string
  content: string
}
