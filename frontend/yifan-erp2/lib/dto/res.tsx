export default interface Res<T> {
  successCode: string,
  statusCode: number,
  msg: string,
  body: T
}
