'use client'
import {Input, Textarea} from "@nextui-org/input";
import {Button, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import {FormEvent, useEffect, useState} from "react";
import Suggestion from "@/app/dto/suggestion";
import Res from "@/app/dto/res";
import myFetch from "@/app/myFetch";

export default function Page() {
  const [sortedSuggestions, setSortedSuggestions] = useState<Suggestion[]>([]);
  useEffect(() => {
    myFetch('/api/suggestion/list')
      .then((res) => res.json())
      .then((data:Res<Suggestion[]>) => {
        if("success"===data.successCode){
          setSortedSuggestions(data.body)
        }else{
          alert(data.msg)
        }
      })
  }, [])

  async function onSubmitHandler(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = {
      email: e.currentTarget.email.value,
      phone: e.currentTarget.phone.value,
      content: e.currentTarget.content.value,
      solved: e.currentTarget.solved.value,
    };

    const response = await myFetch('/api/suggestion', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(formData)
    });
    const data:Res<Suggestion> = await response.json();
    if("success"===data.successCode) {
      window.location.reload()
    }else{
      alert(data.msg)
    }
  }

  return (
    <div className={"mx-4"}>
      <form onSubmit={onSubmitHandler} className={"min-w-96 w-1/2"} aria-label={"Suggestion Form"}>
        <div className={"flex gap-4"}>
          <Input type="text" size={"sm"} label={"email"} name={"email"} placeholder={"你的电子邮箱（选填）"}></Input>
          <Input type="text" size={"sm"} label={"phone"} name={"phone"} placeholder={"你的手机号（选填）"}></Input>
          <input type="hidden" required name="solved" defaultValue={"0"}/>
        </div>
        <Textarea
          className={"mt-2"}
          minRows={10}
          label={"bug details"}
          name="content"
          required
          placeholder={"更新建议描述（必填）"}
        />
        <div className={"mt-2 flex gap-4"}>
          <Button type={"submit"}>提交</Button>
          <Button type={"reset"}>重填</Button>
        </div>
      </form>
      <div className={"mt-4"}>
        <Table
          isStriped
          removeWrapper
        >
          <TableHeader>
            <TableColumn>time</TableColumn>
            <TableColumn>content</TableColumn>
            <TableColumn>feedback</TableColumn>
          </TableHeader>
          <TableBody emptyContent={"目前没有任何更新建议，您可以通过本页面表单进行提交"}>
            {sortedSuggestions.map((suggestion) => (
              <TableRow key={suggestion.id}>
                <TableCell>{suggestion.createTime.toString()}</TableCell>
                <TableCell>{suggestion.content}</TableCell>
                <TableCell>{suggestion.feedback}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
