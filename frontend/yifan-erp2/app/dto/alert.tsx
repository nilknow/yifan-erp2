export default interface Alert{
  id:number,
  content:string,
  // 0:未处理 1:处理中 2:已处理
  state:number,
  // 0:未发送 1:已发送
  emailSent:number;
}
