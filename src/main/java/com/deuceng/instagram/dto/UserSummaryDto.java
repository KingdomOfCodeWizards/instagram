package com.deuceng.instagram.dto;

//userin özet versiyonu.frontendde postla ilgili response dönerken
// userin her bilgisine ihtiyacımız yok o yüzden bunu kullanıyoruz

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private Long id;
    private String username;
    private String profileUrl;
}
