import {
  Select, SelectItem, Spacer,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip
} from "@nextui-org/react";
import Alert from "@/lib/dto/alert";
import Res from "@/lib/dto/res";
import React, {useEffect, useState} from "react";
import myFetch from "@/lib/myFetch";
import EmailAddressModifyModalButton from "@/lib/alert/emailAddressModifyModalButton";
import MaterialAddModalButton from "@/lib/material/addModalButton";

export default function Index() {
  const stateMap: Record<number, string> = {
    0: '未处理',
    1: '处理中',
    2: '已处理'
  };
  const [emailAddress, setEmailAddress] = useState<string>('')
  const [alerts, setAlerts] = useState<Alert[]>([])


  useEffect(() => {
    myFetch("/api/alert/email")
      .then(resp => resp.json())
      .then((data: Res<string>) => {
        if ("success" === data.successCode) {
          setEmailAddress(data.body)
        } else {
          alert(data.msg)
        }
      })
    myFetch("/api/alert/list")
      .then(resp => resp.json())
      .then((data: Res<Alert[]>) => {
        if ("success" === data.successCode) {
          setAlerts(data.body)
        } else {
          alert(data.msg)
        }
      })
  }, [])

  function stateChangeHandler(e: React.ChangeEvent<HTMLSelectElement>, alertId: number) {
    const selectedState = e.target.value;
    myFetch(`/api/alert/${alertId}?state=${selectedState}`, {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({}),
    }).then(resp => resp.json())
      .then((data: Res<Alert[]>) => {
        if ("success" !== data.successCode) {
          alert(data.msg)
        }
      })
  }

  return (
    <div className={"mx-4"}>
      <div className={"flex mb-3"}>
        <MaterialAddModalButton></MaterialAddModalButton>
        <Spacer x={2}/>
        <EmailAddressModifyModalButton></EmailAddressModifyModalButton>
        <Spacer x={2}/>
        <p>预警邮件会发送到：{emailAddress}</p>
      </div>
      <Table>
        <TableHeader>
          <TableColumn>预警编号</TableColumn>
          <TableColumn>预警内容</TableColumn>
          <TableColumn>当前状态</TableColumn>
        </TableHeader>
        <TableBody>
          {alerts.map((alert) => (
            <TableRow key={alert.id}>
              <TableCell>{alert.id}</TableCell>
              <TableCell>{alert.content}</TableCell>
              <TableCell>
                <Select
                  defaultSelectedKeys={["" + alert.state]}
                  onChange={(e) => stateChangeHandler(e, alert.id)}
                >
                  {Object.keys(stateMap).map((key) => (
                    <SelectItem key={"" + Number(key)} value={stateMap[Number(key)]}>
                      {stateMap[Number(key)]}
                    </SelectItem>
                  ))}
                </Select>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
