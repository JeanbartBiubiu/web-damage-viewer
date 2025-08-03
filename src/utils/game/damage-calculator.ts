import { ElMessage } from "element-plus"

// 伤害事件接口
interface DamageEvent {
  id: string
  virtualTime: number // 虚拟时间点
  damage: number
  source: string
  target: string
  type: string
}

// 时间令牌接口
interface TimeToken {
  startTime: number
  duration: number
  endTime: number
}

// 最小堆实现
class MinHeap {
  private heap: DamageEvent[] = []

  constructor() {
    this.heap = []
  }

  private getParentIndex(index: number): number {
    return Math.floor((index - 1) / 2)
  }

  private getLeftChildIndex(index: number): number {
    return 2 * index + 1
  }

  private getRightChildIndex(index: number): number {
    return 2 * index + 2
  }

  private swap(index1: number, index2: number): void {
    ;[this.heap[index1], this.heap[index2]] = [this.heap[index2], this.heap[index1]]
  }

  private heapifyUp(index: number): void {
    const parentIndex = this.getParentIndex(index)
    if (parentIndex >= 0 && this.heap[index].virtualTime < this.heap[parentIndex].virtualTime) {
      this.swap(index, parentIndex)
      this.heapifyUp(parentIndex)
    }
  }

  private heapifyDown(index: number): void {
    const leftChildIndex = this.getLeftChildIndex(index)
    const rightChildIndex = this.getRightChildIndex(index)
    let smallestIndex = index

    if (
      leftChildIndex < this.heap.length &&
      this.heap[leftChildIndex].virtualTime < this.heap[smallestIndex].virtualTime
    ) {
      smallestIndex = leftChildIndex
    }

    if (
      rightChildIndex < this.heap.length &&
      this.heap[rightChildIndex].virtualTime < this.heap[smallestIndex].virtualTime
    ) {
      smallestIndex = rightChildIndex
    }

    if (smallestIndex !== index) {
      this.swap(index, smallestIndex)
      this.heapifyDown(smallestIndex)
    }
  }

  public push(event: DamageEvent): void {
    this.heap.push(event)
    this.heapifyUp(this.heap.length - 1)
  }

  public pop(): DamageEvent | undefined {
    if (this.heap.length === 0) return undefined

    const min = this.heap[0]
    this.heap[0] = this.heap[this.heap.length - 1]
    this.heap.pop()
    this.heapifyDown(0)

    return min
  }

  public peek(): DamageEvent | undefined {
    return this.heap[0]
  }

  public size(): number {
    return this.heap.length
  }
}

// 伤害计算器类
export class DamageCalculator {
  private eventQueue: MinHeap
  private timeTokens: Map<string, TimeToken>
  private currentVirtualTime: number

  constructor() {
    this.eventQueue = new MinHeap()
    this.timeTokens = new Map()
    this.currentVirtualTime = 0
  }

  // 创建时间令牌
  public createTimeToken(duration: number): TimeToken {
    const token: TimeToken = {
      startTime: this.currentVirtualTime,
      duration,
      endTime: this.currentVirtualTime + duration
    }
    return token
  }

  // 检查时间令牌是否有效
  public isTimeTokenValid(token: TimeToken): boolean {
    return this.currentVirtualTime < token.endTime
  }

  // 获取下一个有效时间
  public getNextValidTime(token: TimeToken): number {
    return token.endTime
  }

  // 添加伤害事件
  public addDamageEvent(event: DamageEvent): void {
    this.eventQueue.push(event)
  }

  // 推进虚拟时间到下一个事件
  public advanceToNextEvent(): DamageEvent | undefined {
    const nextEvent = this.eventQueue.peek()
    if (!nextEvent) return undefined

    this.currentVirtualTime = nextEvent.virtualTime
    return this.eventQueue.pop()
  }

  // 获取当前虚拟时间
  public getCurrentVirtualTime(): number {
    return this.currentVirtualTime
  }

  // 获取所有待处理事件
  public getPendingEvents(): DamageEvent[] {
    const events: DamageEvent[] = []
    while (this.eventQueue.size() > 0) {
      const event = this.eventQueue.pop()
      if (event) events.push(event)
    }
    return events
  }
}

// 使用示例
export const createDamageCalculator = () => {
  const calculator = new DamageCalculator()

  // 创建时间令牌示例
  const _token = calculator.createTimeToken(5) // 5个时间单位的有效期

  // 添加伤害事件示例
  const damageEvent: DamageEvent = {
    id: "1",
    virtualTime: 2, // 在虚拟时间点2发生
    damage: 100,
    source: "player1",
    target: "enemy1",
    type: "physical"
  }

  calculator.addDamageEvent(damageEvent)

  return calculator
}
