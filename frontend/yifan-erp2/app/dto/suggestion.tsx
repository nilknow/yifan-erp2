
export default interface Suggestion {
  id: number;
  email: string;
  phone: string;
  priority: string;
  content: string;
  createTime: Date;
  companyId: number;
  feedback:string;
}
