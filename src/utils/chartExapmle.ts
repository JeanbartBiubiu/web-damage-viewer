export class IndivExample{
  Attk!: number;
  Speed!: number;
  HuJia!: number;
  Chuantou!: number;
  ChuantouBaifenbi!: number;
  Baoji!: number;

  Attack(attkObject:IndivExample){
    if (Math.random() >= this.Baoji){
      return this.Attk*(100/((100+((attkObject.HuJia-this.Chuantou)*(1-this.ChuantouBaifenbi))))) * 1.75
    } else{
      return this.Attk*(100/((100+((attkObject.HuJia-this.Chuantou)*(1-this.ChuantouBaifenbi)))))
    }
  }
}
