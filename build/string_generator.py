import argparse

def main():
    
    parser = argparse.ArgumentParser(description='Create some strings')
    parser.add_argument('-s','--string', nargs='+',
                    help='The input strings')
    parser.add_argument('-l', '--length', type=int, nargs=1,
                    help='The amount of times the string will be concatanated')
    parser.add_argument('-p', '--position', nargs='*',
                    help='')

    args = parser.parse_args()

    if(args.string and args.length):
        if(len(args.string) == 1):
            for _ in range(int(args.length[0]/2)):
                print(args.string[0], end="")
            print("")
        elif(len(args.string) == 2):
            for _ in range(int(args.length[0]/2)):
                print(args.string[0], end="")
            for _ in range(int(args.length[0]/2)):
                print(args.string[1], end="")
            print("")
        elif(len(args.string) == 3 and args.position):
            if(args.position[0] == "f"):
                print(args.string[2], end="")
            for _ in range(int(args.length[0]/2)):
                print(args.string[0], end="")
                print(args.string[1], end="")
            if(args.position[0] == "b"):
                print(args.string[2], end="")
            print("")
    else:
        print("Missing arguments...")

if __name__ == "__main__":
    main()

