'use client'
import {Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import React, {useEffect} from "react";
import Order from "@/app/dto/order";
import Res from "@/app/dto/res";
import myFetch from "@/app/myFetch";
import AddModalButton from "@/app/lib/product/order/addModalButton";

export default function OrderTable() {
  const [orders, setOrders] = React.useState<Order[]>([])

  useEffect(() => {
    myFetch('/api/order')
      .then(res => res.json())
      .then((data: Res<Order[]>) => {
        if ("success" === data.successCode) {
          setOrders(data.body)
        } else {
          alert(data.msg)
        }
      })
  }, [])

  return (
    <div className={"mx-4"}>
      <div className={"mb-3"}>
        <AddModalButton></AddModalButton>
      </div>
      <Table>
        <TableHeader>
          <TableColumn>合同编号</TableColumn>
          <TableColumn>订单日期</TableColumn>
          <TableColumn>订单产品</TableColumn>
          {/*<TableColumn>参数要求</TableColumn>*/}
          <TableColumn>数量</TableColumn>
          <TableColumn>生产预计耗时</TableColumn>
          <TableColumn>发货日期</TableColumn>
          <TableColumn>发货状态</TableColumn>
          <TableColumn>客户名称</TableColumn>
          <TableColumn>快递单号</TableColumn>
          <TableColumn>备注</TableColumn>
        </TableHeader>
        <TableBody>
          {orders.map((order) => (
            <TableRow key={order.id}>
              <TableCell>{order.id}</TableCell>
              <TableCell>{order.serialNum}</TableCell>
              <TableCell>{order.product.name}</TableCell>
              <TableCell>{order.count}</TableCell>
              <TableCell>{order.produceDaysTake}</TableCell>
              <TableCell>{new Date(order.delivery.planDate).toLocaleTimeString()}</TableCell>
              <TableCell>{order.delivery.state}</TableCell>
              <TableCell>{order.customer}</TableCell>
              <TableCell>{order.orderSerialNum}</TableCell>
              <TableCell>{order.note}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
