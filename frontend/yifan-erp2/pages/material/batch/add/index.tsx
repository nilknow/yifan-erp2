import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Table, TableBody, TableCell, TableColumn, TableHeader, TableRow,
  useDisclosure
} from "@nextui-org/react";
import React, {useState} from "react";
import Material from "@/lib/dto/material";
import Res from "@/lib/dto/res";
import {Input} from "@nextui-org/input";
import myFetch from "@/lib/myFetch";

interface ConfirmMap {
  toAdd: Material[],
  toUpdate: Material[],
  toDelete: Material[],
}

export default function Index() {
  const tModal = useDisclosure();
  const [confirmMap, setConfirmMap] = React.useState<ConfirmMap>();
  const [selectedFile, setSelectedFile] = useState<File | undefined>();


  function inputFileChangeHandler(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    setSelectedFile(file);
  }

  async function previewHandler() {
    if (!selectedFile) {
      alert("请先选择用于批量添加的Excel文件，您可以点击批量添加模板按钮下载模板用于参考")
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile); // Key and value for backend access

    await myFetch('/api/material/batch/preview', {
      method: 'POST',
      body: formData,
    }).then((res) => res.json())
      .then((data: Res<ConfirmMap>) => {
        if ("success" === data.successCode) {
          setConfirmMap(data.body)
          tModal.onOpen()
        } else {
          alert(data.msg)
        }
        tModal.onOpen()
      })
  }

  async function batchAddHandler(e: React.FormEvent<HTMLFormElement> ){
    e.preventDefault()

    if (!selectedFile) {
      alert("请先选择用于批量添加的Excel文件")
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    await myFetch('/api/material/batch', {
      method: 'POST',
      body: formData,
    }).then((res) => res.json())
      .then((data: Res<ConfirmMap>) => {
        if ("success" === data.successCode) {
          console.log(">>>>>")
          window.location.reload()
        } else {
          alert(data.msg)
        }
      })
  }

  async function jsonBatchAddHandler(){
    if (!selectedFile) {
      alert("请先选择用于批量添加的Excel文件，您可以点击批量添加模板按钮下载模板用于参考")
      return;
    }
    if (!confirmMap) {
      alert("请先选择用于批量添加的Excel文件，您可以点击批量添加模板按钮下载模板用于参考")
      return;
    }

    await myFetch('/api/material/batch', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(confirmMap)
    }).then((res) => res.json())
      .then((data: Res<ConfirmMap>) => {
        if ("success" === data.successCode) {
          console.log(">>>>>")
          window.location.reload()
        } else {
          alert(data.msg)
        }
        tModal.onOpen()
      })
  }

  return (
    <>
      <div className={"mx-4"}>
        <Button
          onClick={() => window.location.href = '/api/material/excel/template'}
          className={"mb-4"}
        >下载批量添加模板</Button>
        <br/>
        <input onChange={inputFileChangeHandler}
               type={"file"}
               required={true}
               accept=".xlsx, .xls"
               placeholder={"请上传批量添加Excel文件"}></input>
        <Button onClick={previewHandler}>批量添加</Button>
        <Modal backdrop={"blur"} isOpen={tModal.isOpen} onOpenChange={tModal.onOpenChange}>
          <ModalContent>
            {(onClose) => (
              <>
                <form onSubmit={batchAddHandler}>
                  <ModalHeader className="flex flex-col gap-1">您确定要添加以下物料吗</ModalHeader>
                  <ModalBody>
                    <Table>
                      <TableHeader>
                        <TableColumn>SerialNum</TableColumn>
                        <TableColumn>Name</TableColumn>
                        <TableColumn>Category</TableColumn>
                        <TableColumn>Count</TableColumn>
                        <TableColumn>Inventory Alert</TableColumn>
                      </TableHeader>
                      <TableBody>
                        {
                          confirmMap ?
                            confirmMap.toAdd ?
                              confirmMap.toAdd.map((material: Material) => (
                                <TableRow key={material.serialNum}>
                                  <TableCell>{material.serialNum}</TableCell>
                                  <TableCell>{material.name}</TableCell>
                                  <TableCell>{material.category}</TableCell>
                                  <TableCell>{material.count}</TableCell>
                                  <TableCell>{material.inventoryCountAlert}</TableCell>
                                </TableRow>
                              )) : <></>
                            : <></>
                        }
                      </TableBody>
                    </Table>
                  </ModalBody>
                  <ModalFooter>
                    <Button type={"submit"}>确定</Button>
                    <Button type={"button"} onClick={onClose}>取消</Button>
                  </ModalFooter>
                </form>
              </>
            )}
          </ModalContent>
        </Modal>
      </div>
    </>
  )
}
