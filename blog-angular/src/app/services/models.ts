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
  pageNumber: number
  totalPages: number
  isFirst: boolean
  isLast: boolean
  hasNext: boolean
  hasPrevious: boolean
}

export interface PostUserView {
  id: number
  title: string
  url: string
  content: string
  createdBy: User
  createdAt: Date
  editable: boolean
}


export interface User {
  id: number
  name: string
  email: string
  role: string
}
