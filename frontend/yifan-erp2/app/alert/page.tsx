'use client'
import {
  Button,
  Link, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Select, SelectItem, Spacer,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip, useDisclosure
} from "@nextui-org/react";
import Alert from "@/app/dto/alert";
import Res from "@/app/dto/res";
import React, {FormEvent, useEffect, useState} from "react";
import myFetch from "@/app/myFetch";
import EmailAddressModifyModalButton from "@/app/lib/alert/emailAddressModifyModalButton";
import MaterialAddModalButton from "@/app/lib/material/addModalButton";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";

export default function Page() {
  const stateMap: Record<number, string> = {
    0: '未处理',
    1: '处理中',
    2: '已处理'
  };
  const [emailAddress, setEmailAddress] = useState<string>('')
  const [alerts, setAlerts] = useState<Alert[]>([])
  const modal = useDisclosure();


  useEffect(() => {
    myFetch("/api/alert/email")
      .then(resp => resp.json())
      .then((data: Res<string>) => {
        if ("success" === data.successCode) {
          if (data.body == "") {
            modal.onOpen()
          } else {
            setEmailAddress(data.body)
          }
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

  async function alertEmailHandler(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = {
      address: e.currentTarget.address.value,
    };
    const response = await myFetch('/api/alert/email', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(formData)
    })
      .then((resp)=>resp.json())
      .then((data:Res<string|null>)=>{
        if (data.successCode == "success") {
          window.location.reload()
        }else{
          alert(data.msg)
        }
      })
  }

  return (
    <div className={"mx-4"}>
      <div className={"flex mb-3"}>
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

      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={alertEmailHandler}>
                <ModalHeader className="flex flex-col gap-1">设置预警邮箱（逗号分隔）</ModalHeader>
                <ModalBody>
                  <Input type={"string"} name={"address"} defaultValue={""}
                         label={"邮箱地址"}/>
                </ModalBody>
                <ModalFooter>
                  <Button color="default" variant="light" onPress={onClose}>
                    取消
                  </Button>
                  <Button type="submit" color="default" onPress={onClose}>
                    添加
                  </Button>
                </ModalFooter>
              </form>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  )
}
