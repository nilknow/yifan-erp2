
export default interface Bug {
  id: number;
  email: string;
  phone: string;
  priority: string;
  content: string;
  createTime: Date;
  companyId: number;
  feedback:string;
}
