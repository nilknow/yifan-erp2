'use client'
import {Input, Textarea} from "@nextui-org/input";
import {Button, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import {FormEvent, useEffect, useState} from "react";
import Bug from "@/app/dto/bug";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";

export default function Page() {
  const [sortedBugs, setSortedBugs] = useState<Bug[]>([]);

  useEffect(() => {
    myFetch('/api/bug/list')
      .then((res) => res.json())
      .then((data:Res<Bug[]>) => {
        if("success"==data.successCode){
          setSortedBugs(data.body)
        }
      })
  }, [])

  async function onSubmitHandler(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = {
      email: e.currentTarget.email.value,
      phone: e.currentTarget.phone.value,
      content: e.currentTarget.content.value,
      priority: e.currentTarget.priority.value,
    };

    const response = await myFetch('/api/bug/save', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(formData)
    });
    const data = await response.json();
    setSortedBugs(data);
  }

  return (
    <div className={"mx-4"}>
      <form onSubmit={onSubmitHandler} className={"min-w-96 w-1/2"}>
        <div className={"flex gap-4"}>
          <Input type="text" size={"sm"} label={"email"} name={"email"} placeholder={"你的电子邮箱（选填）"}></Input>
          <Input type="text" size={"sm"} label={"phone"} name={"phone"} placeholder={"你的手机号（选填）"}></Input>
          <input type="hidden" required name="priority" value="一般"/>
        </div>
        <Textarea
          className={"mt-2"}
          minRows={10}
          label={"bug details"}
          name="content"
          required
          placeholder={"bug详情描述（必填）"}
        />
        <div className={"mt-2 flex gap-4"}>
          <Button type={"submit"}>提交</Button>
          <Button type={"reset"}>重填</Button>
        </div>
      </form>
      <div className={"mt-4"}>
        <Table isStriped>
          <TableHeader>
            <TableColumn>time</TableColumn>
            <TableColumn>content</TableColumn>
            <TableColumn>feedback</TableColumn>
          </TableHeader>
          <TableBody emptyContent={"目前没有任何bug反馈，您可以通过本页面表单进行提交"}>
            {sortedBugs
              ? sortedBugs.map((bug) => (
                <TableRow key={bug.id}>
                  <TableCell>{bug.createTime.toString()}</TableCell>
                  <TableCell>{bug.content}</TableCell>
                  <TableCell>{bug.feedback}</TableCell>
                </TableRow>))
              : <></>}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
