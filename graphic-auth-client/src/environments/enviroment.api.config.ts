import {environment} from './environment';

export const API_CONFIG_URI ={
    AUTH_LOGIN :`${environment.baseUrl}/login` ,
    CREATE_ACOUNT: `${environment.baseUrl}/auth/sign-up` ,
    IMAGE_UPLOAD: `${environment.baseUrl}/image/upload` ,
    VERYFY_TOTP: `${environment.baseUrl}/auth/totp/verify` ,
}