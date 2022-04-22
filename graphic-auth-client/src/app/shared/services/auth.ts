export class Auth{

  private _acessToken: string = "";
  private _refreshToken: string = "";
  
  public get acessToken(): string {
    return this._acessToken;
  }
  public set acessToken(value: string) {
    this._acessToken = value;
  }
  
  public get refreshToken(): string {
    return this._refreshToken;
  }
  public set refreshToken(value: string) {
    this._refreshToken = value;
  }

}