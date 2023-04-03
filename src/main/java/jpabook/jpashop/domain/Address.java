package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable //어딘가에 내장될 수 있음
@Getter
@AllArgsConstructor //setter대신 생성자만 둬서 수정할 수 없도록 함. Embeded타입은 자바 기본 생성자를 public 또는 protected로 둬야 하는데,
//protected로 두는것이 그나마 더 안전하다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){}
}
