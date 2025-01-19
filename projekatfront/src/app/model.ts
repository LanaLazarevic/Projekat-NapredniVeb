export interface LoginRequest {
  email: string,
  password: string
}

export interface LoginResponse {
  jwt: string,
  can_read_users: boolean,
  can_create_users: boolean,
  can_delete_users: boolean,
  can_update_users: boolean
}

export interface User {
  firstname: string,
  lastname:string,
  email:string,
  id: number,
  permissions: Set<String>
}

export interface UserCreate {
  firstname: string,
  lastname:string,
  email:string,
  password: string,
  permissions: Set<String>
}

export interface Order{
  id: number;
  createdBy: User;
  status: string;
  active: boolean;
  orderedAt: Date;
  dishes: string;
}

export interface OrderFilter{
  from:string;
  to:string;
  email:string,
  // @ts-ignore
  statuses: List<String>,
}

export interface Dish{
  id:number,
  name: string,
  price:number
}

export interface Error{
  errorId:number,
  forOrder: string,
  user: string,
  message:string,
  time:string;
  operation:string
}
