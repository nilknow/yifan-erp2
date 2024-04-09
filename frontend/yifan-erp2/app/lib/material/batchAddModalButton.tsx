'use client'
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  SelectItem, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure
} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import React, {useRef, useState} from "react";
import Res from "@/app/dto/res";
import Material from "@/app/dto/material";
import myFetch from "@/app/myFetch";

interface ConfirmMap {
  toAdd: Material[],
  toUpdate: Material[],
  toDelete: Material[],
}

export default function BatchAddModalButton() {
  const modal = useDisclosure();
  const confirmModal = useDisclosure();
  const formRef = useRef<HTMLFormElement>(null);
  const [confirmMap,setConfirmMap] = React.useState<ConfirmMap>();

  async function confirmHandler(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault();

    let current = formRef.current;
    console.log(formRef)
    console.log(current)
    let files = current?.files;
    console.log(files)
    const file=files?.[0]
    if(file){
      const formData=new FormData()
      formData.append('file',file)
      myFetch('/api/material/batch/preview', {
        method: 'POST',
        body: formData,
      })
        .then((res) => res.json())
        .then((data: Res<ConfirmMap>) => {
          if ("success" === data.successCode) {
            confirmModal.onOpen()
          } else {
            alert(data.msg)
          }
        })
    }else{
      alert("请先选择要上传的用于批量导入的文件")
    }
  }

  const handleFileChange = () => {
    const file = formRef.current?.files[0];

    if (file) {
      const reader = new FileReader();

      reader.onload = (event) => {
        const fileData = event.target?.result;
        // Do something with the file data
        console.log('Selected file:', file);
        console.log('File data:', fileData);
      };

      reader.readAsDataURL(file);
    }
  };

  async function batchAddHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault(); // Prevent default form submission behavior

    let formData = e.currentTarget;
    myFetch('/api/material/batch', {
      method: 'POST',
      body: new FormData(formData),
    })
      .then((res) => res.json())
      .then((data: Res<string>) => {
        if ("success" === data.successCode) {
          window.location.reload()
        } else {
          alert(data.msg)
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
        批量添加
      </Button>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form ref={formRef} onSubmit={confirmHandler}>
                <ModalHeader className="flex flex-col gap-1">批量添加物料</ModalHeader>
                <ModalBody>
                  <Button onClick={() => window.location.href = '/api/material/excel/template'}>下载模板</Button>
                  <Input
                    onChange={handleFileChange}
                    label={"选择批量添加用的的Excel文件"}
                    name={"file"}
                    type={"file"}
                    required={true}
                    accept=".xlsx, .xls"
                    placeholder={"请上传批量添加Excel文件"}>
                  </Input>
                </ModalBody>
                <ModalFooter>
                  <Button color="default" variant="light" onPress={onClose}>
                    取消
                  </Button>
                  <Button type="submit" color="default" onPress={onClose}>
                    添加
                  </Button>
                  <Modal backdrop={"blur"} isOpen={confirmModal.isOpen} onOpenChange={confirmModal.onOpenChange}>
                    <ModalContent>
                      {(onClose) => (
                        <>
                          <form onSubmit={batchAddHandler}>
                            <ModalHeader className="flex flex-col gap-1">请确认需要添加的物料</ModalHeader>
                            <ModalBody>
                              <Table>
                                <TableHeader>
                                  <TableColumn>Action</TableColumn>
                                  <TableColumn>SerialNum</TableColumn>
                                  <TableColumn>Name</TableColumn>
                                  <TableColumn>Category</TableColumn>
                                  <TableColumn>Count</TableColumn>
                                  <TableColumn>Inventory Alert</TableColumn>
                                </TableHeader>
                                <TableBody>
                                  {
                                    confirmMap?
                                      confirmMap.toAdd ?
                                      confirmMap.toAdd.map((material: Material) => (
                                        <TableRow key={material.id}>
                                          <TableCell>To Add</TableCell>
                                          <TableCell>{material.serialNum}</TableCell>
                                          <TableCell>{material.name}</TableCell>
                                          <TableCell>{material.category}</TableCell>
                                          <TableCell>{material.count}</TableCell>
                                          <TableCell>{material.inventoryCountAlert}</TableCell>
                                        </TableRow>
                                      )): <></>
                                      :<></>
                                  }
                                  {/*{*/}
                                  {/*  confirmMap?*/}
                                  {/*    confirmMap.toUpdate ?*/}
                                  {/*    confirmMap.toUpdate.map((material: Material) => (*/}
                                  {/*      <TableRow key={material.id}>*/}
                                  {/*        <TableCell>To Update</TableCell>*/}
                                  {/*        <TableCell>{material.serialNum}</TableCell>*/}
                                  {/*        <TableCell>{material.name}</TableCell>*/}
                                  {/*        <TableCell>{material.category}</TableCell>*/}
                                  {/*        <TableCell>{material.count}</TableCell>*/}
                                  {/*        <TableCell>{material.inventoryCountAlert}</TableCell>*/}
                                  {/*      </TableRow>*/}
                                  {/*    )): <></>*/}
                                  {/*    :<></>*/}
                                  {/*}*/}
                                  {/*{*/}
                                  {/*  confirmMap?*/}
                                  {/*    confirmMap.toDelete ?*/}
                                  {/*    confirmMap.toDelete.map((material: Material) => (*/}
                                  {/*      <TableRow key={material.id}>*/}
                                  {/*        <TableCell>To Delete</TableCell>*/}
                                  {/*        <TableCell>{material.serialNum}</TableCell>*/}
                                  {/*        <TableCell>{material.name}</TableCell>*/}
                                  {/*        <TableCell>{material.category}</TableCell>*/}
                                  {/*        <TableCell>{material.count}</TableCell>*/}
                                  {/*        <TableCell>{material.inventoryCountAlert}</TableCell>*/}
                                  {/*      </TableRow>*/}
                                  {/*    )): <></>*/}
                                  {/*    :<></>*/}
                                  {/*}*/}
                                </TableBody>
                              </Table>
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
                      )
                      }
                    </ModalContent>
                  </Modal>
                </ModalFooter>
              </form>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
