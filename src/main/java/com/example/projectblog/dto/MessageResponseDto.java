package com.example.projectblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//모든 필드값을 파라미터로 받는 생성자 생성
@AllArgsConstructor
//파라미터가 없는 기본생성자를 생성
@NoArgsConstructor
//해당 클래스에 자동으로 빌더를 추가
@Builder
public class MessageResponseDto {

  //필드
  private String message;          //BoardResponseDto 에 있던 필드와 생성자를 별도로 ResponseDto 를 만들어서 분리해줄 수도 있다!
  private int statusCode;          //기능에 따라서 반환하려는 데이터가 다른 경우에!

  //@AllArgsConstructor 덕분에, 모든 필드값을 파라미터로 받는 생성자를 굳이 생성해주지 않아도 됨
//    public MsgResponseDto(String msg, int statusCode) {
//        this.msg = msg;
//        this.statusCode = statusCode;
//    }
}